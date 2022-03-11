import asyncio
import sqlite3
import socket


class Client:
    def __init__(self, socket, address):
        self.socket, self.addres = socket, address
