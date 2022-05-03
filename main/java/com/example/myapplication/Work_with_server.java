package com.example.myapplication;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Work_with_server {
    private static Socket clientSocket;
    private static Scanner in;
    private static PrintWriter out;

    public String send_get(String request) {
        try {
            try {
                clientSocket = new Socket();
                clientSocket.connect(new InetSocketAddress("localhost", 4004));
                //имя хоста и порт потом поменяем
                in = new Scanner(clientSocket.getInputStream());
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                out.write(request);

                return in.nextLine();
            } finally {
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(e);
            return "error";
        }
    }
}

