import server
import asyncio


ADDRESS = '127.0.0.1'
PORT = 9999

ser = server.Server('database.db', ADDRESS, PORT)
