import asyncio
import aiosqlite
import socket
from client import Client

MESSAGE_WEIGHT = 2048

AUTH, CONSULTATION, SEARCH, ACCEPT, DECLINE, SGNIN = 'client_enter', 'need_consultation', 'srch', 'accept', 'do_not_accept', 'client_registration'
D_AUTH, D_SIGNIN = 'doctor_enter', 'doctor_registration'
DELIMITER = ' '


class Server:
    def __init__(self, database, connection_address, connection_port):
        self.database_address, self.connection_addres, self.connection_port = database, connection_address, connection_port

        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

        self.socket.bind((self.connection_addres, self.connection_port))

        self.users, self.loop = [], asyncio.get_event_loop()

        self.clients_active = dict()

        self.socket.listen(5)

        tasks = [asyncio.ensure_future(self.accept_new_connections()), asyncio.ensure_future(self.connect_to_db()), asyncio.ensure_future(self.listen_client())]

        while True:
            self.loop.run_until_complete(asyncio.gather(*tasks))

    async def connect_to_db(self):
        self.database = await aiosqlite.connect(self.database_address)

    async def accept_new_connections(self):
        await self.listen_client()
        client_socket, client_addres = self.socket.accept()

        print('NEW CONNECTION: client addres = ', client_addres)

        # client = Client(client_socket, client_addres)

        self.clients_active[client_addres] = client_socket

        return client_socket, client_addres

    async def listen_client(self):
        for client in self.clients_active.keys():
            client_socket = self.clients_active[client]
            message = await client_socket.recv(MESSAGE_WEIGHT)

            print('NEW MESSAGE FROM', client_socket, message)

            mode, *info = str(message.decode('UTF-8')).split(DELIMITER)

            if mode == AUTH or mode == D_AUTH:
                is_user = 1 if mode == AUTH else 0
                username, usersurname, email, password, gender, age, date_of_birth = info
                id = await self.check_auth_data(is_user, email, password)
                if id != -1:
                    self.clients_active[id] = client_socket
                    await self.send_message_to_client('has same user', client_socket=client_socket)
                else:
                    await self.send_message_to_client('not same user', client_socket=client_socket)

            if mode == SGNIN or mode == D_SIGNIN:
                is_user = 1 if mode == SGNIN else 0
                result = await self.sign_in_new_user(is_user, info)
                await self.send_message_to_client(result, client_socket=client_socket)

            elif mode == SEARCH:
                specialization, firstName, middleName, lastName, stage = info
                result = await self.search_doctor(specialization, firstName, middleName, lastName, stage)
                await self.send_message_to_client(result, client_socket=client_socket)

            elif mode == CONSULTATION:
                stage = info
                doctors = await self.search_doctor(stage=stage)
                for doctor in doctors:
                    if doctor in self.clients_active.keys():
                        await self.send_message_to_client('need consultation', client_id=doctor)

            elif mode == ACCEPT or mode == DECLINE:
                client_id = info
                answer = 'accept' if mode == ACCEPT else 'do not accept'
                await self.send_message_to_client(answer, client_id=client_id)

                '''await self.add_new_message_to_chat(chat_id, user_id, text)
                await self.notify_user_about_new_message(is_user, chat_id)'''

            '''elif mode == NEW_CHAT:
                user_id, doctor_id = info
                result = await self.start_new_chat(user_id, doctor_id)
                await self.send_message_to_client(result, user_id)
                if result != -1:
                    await self.send_message_to_client(DELIMITER.join([mode, user_id]), doctor_id)'''

    async def check_auth_data(self, is_user, email, password):
        table = 'users' if is_user else 'doctors'
        # TODO: исправить иньекцию и добавить хеширование пароля
        result = await self.database.execute(
            "SELECT id FROM {table} WHERE email={email} AND password={password};".format(table=table, email=email,
                                                                                         password=password))
        result = list(await result.fetchone())[0]

        return result if result else -1

    async def search_doctor(self, specialization=0, firstName=0, middleName=0, lastName=0, stage=0, urgent=False):
        # TODO: исправить иньекцию и дописать обработку для urgent=True
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
        result = list(await result.fetchall())

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
                for user in self.clients_active:
                    if self.clients_active[user] == client_socket:
                        self.clients_active.pop(user)
                print(e)

    async def get_user_chats(self, is_user, user_id):
        companion = 'doctor_id' if is_user else 'user_id'
        user = 'user_id' if is_user else 'doctor_id'
        result = await self.database.execute('SELECT id, {companion} FROM chats WHERE {user} = {user_id};')
        result = list(await result.fetchall())
        return result if result else -1

    async def start_new_chat(self, user_id, doctor_id):
        try:
            await self.database.execute('INSERT INTO chats values(user_id={user_id}, doctor_id={doctor_id});')
            await self.database.commit()
            result = await self.database.execute(
                f'SELECT id FROM chats WHERE user_id={user_id} and doctor_id={doctor_id};')
            new_chat_id = list(await result.fetchone())[0]
            return new_chat_id
        except Exception as e:
            print(e)
            return -1

    async def notify_user_about_new_message(self, is_user, chat_id):
        companion_type = 'doctor_id' if is_user else 'user_id'
        result = await self.database.execute(f'''SELECT {companion_type} FROM chats WHERE chat_id = {chat_id}''')
        companion = list(await result.fetchone())[0]
        if companion in self.clients_active:
            await self.send_message_to_client(f'new consultation request from {companion}', companion)

    async def sign_in_new_user(self, is_user, info):

        if is_user:
            firstname, lastname, email, password, gender, age, date_of_birth, weight, height = info
            query = 'firstname = {firstname}, lastname = {lastname}, email = {email}, password = {password}, dateofbirth = {dateofbirth},' \
                    ' weight = {weight}, height = {height}, info = {info}'  # здесь может сломаться запрос, потому что тип string, а мы скобочки над {} не ставим
        if not is_user:
            firstname, lastname, email, password, gender, age, date_of_birth, stage = info
            query = 'firstname = {firstname}, lastname = {lastname}, email = {email}, age = {age}, dateofbirth = {date_of_birth} stage = {stage},' \
                    ' password = {password}, info = {info}'

        table = 'users' if is_user else 'doctors'
        already_exists = await self.database.execute("SELECT * from {table} WHERE email={email};")

        if not already_exists:
            await self.database.execute("INSERT INTO {table} VALUES ({query});")
            await self.database.commit()
            result = await self.database.execute(
                "SELECT id FROM {table} WHERE email = {email} AND password = {password};")
            id = list(await result.fetchone())[0]
            return 'not same user'
        else:
            return 'has same user'

