import asyncio
import aiosqlite
import socket
from client import Client


MESSAGE_WEIGHT = 2048

AUTH, SEND_MSG, SEARCH = 'au', 'smg', 'srch'

DELIMITER = '%%'


class Server:
    def __init__(self, database, connection_address, connection_port):
        self.database_address, self.connection_addres, self.connection_port = database, connection_address, connection_port

        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

        self.socket.bind((self.connection_addres, self.connection_port))

        self.users, self.loop = [], asyncio.new_event_loop()

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
            message = await self.loop.sock_recv(MESSAGE_WEIGHT)

            print('NEW MESSAGE FROM', client_socket, message)

            mode, text, *info = str(message.decode('UTF-8')).split(DELIMITER)

    async def check_auth_data(self, is_user, email, password):
        table = 'users' if is_user else 'doctors'
                                                # TODO: исправить иньекцию и добавить хеширование пароля
        result = await self.database.execute(
            "SELECT * FROM {table} WHERE email={email} AND password={password};".format(table=table, email=email, password=password))

        return 1 if result else 0

    async def search_doctor(self, specialization=0, firstName=0, middleName=0, lastName=0, stage=0, urgent=False):
        #TODO: исправить иньекцию и дописать обработку для urgent=True
        query = 'SELECT * FROM doctors WHERE '
        delimiter = ''
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

    async def get_chats(self, id, is_user):
        table = 'users' if is_user else 'doctors'
        result = await self.database.execute('SELECT chats FROM {table} WHERE id={id}'.format(table=table, id=id))
        # TODO: возможно, данные в result будут в кривом формате. Надо проверить и дописать обработку, если вдруг
        return result

