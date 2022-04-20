import asyncio
import sqlite3
import socket


class Client:
    def __init__(self, socket, address, id):
        self.socket, self.addres, self.id = socket, address, id
