package com.example.myapplication;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.*;
//import java.net.UnknownHostException;
import java.util.Scanner;

public class Work_with_server {
    private Socket clintSocket;
    private Scanner in;
    private PrintWriter out;

    public String send_get(String request) {
        try {
            try {
                clintSocket = new Socket("127.0.0.1", 9999);
                //clientSocket.connect(new InetSocketAddress("localhost", 7777));
                //in = new Scanner(clintSocket.getInputStream());
                System.out.println("before writeUTF");
                DataOutputStream out = new DataOutputStream(clintSocket.getOutputStream());

                out.writeUTF("hkjh kjhklhjh lkjhlkjhlkj h lkjh");
                System.out.println("After writeUTF");

                return in.nextLine();
            } finally {
                //clientSocket.close();
                System.out.println("hello");
            }

        }
        catch (UnknownHostException e) {
            System.err.println("Don't know about host ");
            return "error";}
        catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }
}
