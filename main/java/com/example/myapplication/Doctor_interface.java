package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Doctor_interface extends AppCompatActivity {

    private Button give_consultation, no_give_consultation, change_data;
    private Work_with_server work_with_server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_interface);

        change_data = findViewById(R.id.change_data);
        give_consultation = findViewById(R.id.give_consultation);
        no_give_consultation = findViewById(R.id.no_give_consultation);

        try {
            Socket clientSocket = new Socket();
            clientSocket.connect(new InetSocketAddress("localhost", 7777));
            Scanner in = new Scanner(clientSocket.getInputStream());
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            while (true){
                if (in.hasNextLine()){
                    String response = in.nextLine();
                    give_consultation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            out.write("accept " + response);
                        }
                    });
                    no_give_consultation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            out.write("do_not_accept " + response);
                        }
                    });
                }
                change_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Doctor_interface.this, Doctor_data.class);
                        startActivity(intent);
                    }
                });
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}