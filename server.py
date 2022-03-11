import asyncio
import sqlite3
import socket
from client import Client


MESSAGE_WEIGHT = 2048


class Server:
    def __init__(self, database, connection_address, connection_port):
        self.database, self.connection_addres, self.connection_port = database, connection_address, connection_port

        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

        self.socket.bind((self.connection_addres, self.connection_port))

        self.users, self.loop = [], asyncio.new_event_loop()

    async def run(self):
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
