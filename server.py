import asyncio
import aiosqlite
import socket
from client import Client


MESSAGE_WEIGHT = 2048

AUTH, SEND_MSG, SEARCH, NEW_CHAT, SGNIN = 'au', 'smg', 'srch', 'nchat', ''

DELIMITER = '%%'


class Server:
    def __init__(self, database, connection_address, connection_port):
        self.database_address, self.connection_addres, self.connection_port = database, connection_address, connection_port

        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

        self.socket.bind((self.connection_addres, self.connection_port))

        self.users, self.loop = [], asyncio.new_event_loop()

        self.clients_active = dict()

    async def run(self):
        self.database = await aiosqlite.connect(self.database_address)
        await self.loop.create_task(self.accept_new_connections())

    async def accept_new_connections(self):
        while True:
            client_socket, client_addres = await self.loop.sock_accept()

            print('NEW CONNECTION: client addres = ', client_addres)

            client = Client(client_socket, client_addres)

            self.users.append(client)
            self.loop.create_task(self.listen_client(client.socket))

    async def listen_client(self, client_socket):
        while True:
            message = await self.loop.sock_recv(MESSAGE_WEIGHT)     # вот это че за хуйня? где нашел - не пойму. может сломаться

            print('NEW MESSAGE FROM', client_socket, message)

            mode, text, *info = str(message.decode('UTF-8')).split(DELIMITER)

            if mode == AUTH:
                is_user, email, password = info
                id = await self.check_auth_data(is_user, email, password)
                if id != -1:
                    if is_user:
                        self.clients_active[id] = client_socket
                    await self.send_message_to_client(self.get_user_chats(is_user, id), client_socket=client_socket)
                else:
                    await self.send_message_to_client('AUTH_FAILED', client_socket=client_socket)

            if mode == SGNIN:
                result = await self.sign_in_new_user(info)
                await self.send_message_to_client(result, client_socket=client_socket)

            elif mode == SEARCH:
                specialization, firstName, middleName, lastName, stage = info
                result = await self.search_doctor(specialization, firstName, middleName, lastName, stage)
                await self.send_message_to_client(result, client_socket=client_socket)

            elif mode == SEND_MSG:
                user_id, is_user, chat_id = info
                await self.add_new_message_to_chat(chat_id, user_id, text)
                await self.notify_user_about_new_message(is_user, chat_id)

            elif mode == NEW_CHAT:
                user_id, doctor_id = info
                result = await self.start_new_chat(user_id, doctor_id)
                await self.send_message_to_client(result, user_id)
                if result != -1:
                    await self.send_message_to_client(DELIMITER.join([mode, user_id]), doctor_id)

    async def check_auth_data(self, is_user, email, password):
        table = 'users' if is_user else 'doctors'
                                                # TODO: исправить иньекцию и добавить хеширование пароля
        result = await self.database.execute(
            "SELECT id FROM {table} WHERE email={email} AND password={password};".format(table=table, email=email, password=password))

        return result if result else -1

    async def search_doctor(self, specialization=0, firstName=0, middleName=0, lastName=0, stage=0, urgent=False):
        #TODO: исправить иньекцию и дописать обработку для urgent=True
        query = 'SELECT * FROM doctors WHERE '
        delimiter = ''

        if urgent:
            query += 'id IN ({ids});'.format(ids=', '.join(self.clients_active.keys()))

        if specialization != 0:
            query += delimiter + 'specialization= ' + specialization + ' '
            delimiter = 'AND '
        if firstName != 0:
            query += delimiter + 'firstname= ' + firstName + ' '
            delimiter = 'AND '
        if middleName != 0:
            query += delimiter + 'middlename= ' + middleName + ' '
            delimiter = 'AND '
        if lastName != 0:
            query += delimiter + 'lastname= ' + lastName + ' '
            delimiter = 'AND '

        query += delimiter + 'stage>= ' + stage + ';'
        result = await self.database.execute(query)

        return result

    async def send_message_to_client(self, message, client_id=-1, client_socket=-1):
        if client_id != -1:
            try:
                self.clients_active[client_id].sendall(message)
            except Exception as e:
                self.clients_active.pop(client_id)
                print(e)
        elif client_socket != -1:
            try:
                client_socket.sendall(message)
            except Exception as e:
                print(e)        # TODO: здесь чето сломается прям жепой чувтсвую

    async def get_user_chats(self, is_user, user_id):   # TODO: У каждой строки результата менять id доктора/пользователя на имя фамилиюy
        companion = 'doctor_id' if is_user else 'user_id'
        user = 'user_id' if is_user else 'doctor_id'
        result = await self.database.execute('SELECT id, {companion} FROM chats WHERE {user} = {user_id};')
        return result if result else -1

    async def start_new_chat(self, user_id, doctor_id):
        try:
            await self.database.execute('INSERT INTO chats values(user_id={user_id}, doctor_id={doctor_id});')
            await self.database.commit()
            new_chat_id = await self.database.execute(f'SELECT id FROM chats WHERE user_id={user_id} and doctor_id={doctor_id};')
            return new_chat_id
        except Exception as e:
            print(e)
            return -1

    async def add_new_message_to_chat(self, chat_id, sender_id, message):
        try:
            await self.database.execute(f'''UPDATE chats SET messages=messages+{DELIMITER + sender_id+ ':' + message} WHERE id={chat_id};''')
            await self.database.commit()
        except Exception as e:
            print(e)

    async def notify_user_about_new_message(self, is_user, chat_id):
        companion_type = 'doctor_id' if is_user else 'user_id'
        companion = await self.database.execute(f'''SELECT {companion_type} FROM chats WHERE chat_id = {chat_id}''')
        if companion in self.clients_active:
            await self.send_message_to_client(SEND_MSG, companion)  # жалуется... Надо проверить, в каком формате получаем ответ из бд

    async def sign_in_new_user(self, info):
        is_user = info[0]

        if is_user:
            firstname, lastname, email, password, dateofbirth, weight, height, *info = info[1:]
            query = 'firstname = {firstname}, lastname = {lastname}, email = {email}, password = {password}, dateofbirth = {dateofbirth},' \
                    ' weight = {weight}, height = {height}, info = {info}'     # здесь может сломаться запрос, потому что тип string, а мы скобочки над {} не ставим
        if not is_user:
            firstname, middlename, lastname, age, specialization, email, password, stage = info[1:]
            query = 'firstname = {firstname}, middlename = {middlename}, lastname = {lastname}, email = {email}, age = {age}, stage = {stage},' \
                    ' specialization = {specialization}, password = {password}, info = {info}'

        table = 'users' if is_user else 'doctors'
        already_exists = await self.database.execute("SELECT * from {table} WHERE email={email};")

        if not already_exists:
            await self.database.execute("INSERT INTO {table} VALUES ({query});")
            await self.database.commit()
            id = await self.database.execute("SELECT id FROM {table} WHERE email = {email} AND password = {password};")
            return id
        else:
            return 'HAS SAME USER'

