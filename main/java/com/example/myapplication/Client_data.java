package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Client_data extends AppCompatActivity {

    private EditText user_name, user_surname, user_email, user_password,
            user_gender, user_age, user_date_of_birth, client_weight, client_height;
    private Button change_name, change_surname, change_email, change_password,
            change_gender, change_age, change_date_of_birth, change_weight, change_height;
    private Work_with_server work_with_server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_data);

        user_name = findViewById(R.id.user_name);
        user_surname = findViewById(R.id.user_surname);
        user_email = findViewById(R.id.user_email);
        user_password = findViewById(R.id.user_password);
        user_gender = findViewById(R.id.user_gender);
        user_age = findViewById(R.id.user_age);
        user_date_of_birth = findViewById(R.id.user_date_of_birth);
        client_weight = findViewById(R.id.client_weight);
        client_height = findViewById(R.id.client_height);

        change_name = findViewById(R.id.change_name);
        change_surname = findViewById(R.id.change_surname);
        change_email = findViewById(R.id.change_email);
        change_password = findViewById(R.id.change_password);
        change_gender = findViewById(R.id.change_gender);
        change_age = findViewById(R.id.change_age);
        change_date_of_birth = findViewById(R.id.change_date_of_birth);
        change_weight = findViewById(R.id.change_weight);
        change_height = findViewById(R.id.change_height);

        change_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String request = "change name to " + user_name.getText().toString().trim();
                String response = work_with_server.send_get(request);
                if (!response.equals("error")){
                    if(response.equals("successfully")){
                        Toast.makeText(Client_data.this, R.string.successfully,
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(Client_data.this, R.string.unsuccessfully,
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        change_surname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String request = "change surname to " + user_surname.getText().toString().trim();
                String response = work_with_server.send_get(request);
                if (!response.equals("error")){
                    if(response.equals("successfully")){
                        Toast.makeText(Client_data.this, R.string.successfully,
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(Client_data.this, R.string.unsuccessfully,
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        change_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String request = "change email to " + user_email.getText().toString().trim();
                String response = work_with_server.send_get(request);
                if (!response.equals("error")){
                    if(response.equals("successfully")){
                        Toast.makeText(Client_data.this, R.string.successfully,
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(Client_data.this, R.string.unsuccessfully,
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String request = "change password to " + user_password.getText().toString().trim();
                String response = work_with_server.send_get(request);
                if (!response.equals("error")){
                    if(response.equals("successfully")){
                        Toast.makeText(Client_data.this, R.string.successfully,
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(Client_data.this, R.string.unsuccessfully,
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        change_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String request = "change gender to " + user_gender.getText().toString().trim();
                String response = work_with_server.send_get(request);
                if (!response.equals("error")){
                    if(response.equals("successfully")){
                        Toast.makeText(Client_data.this, R.string.successfully,
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(Client_data.this, R.string.unsuccessfully,
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        change_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String request = "change age to " + user_age.getText().toString().trim();
                String response = work_with_server.send_get(request);
                if (!response.equals("error")){
                    if(response.equals("successfully")){
                        Toast.makeText(Client_data.this, R.string.successfully,
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(Client_data.this, R.string.unsuccessfully,
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        change_date_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String request = "change date of birth to " + user_date_of_birth.getText().toString().trim();
                String response = work_with_server.send_get(request);
                if (!response.equals("error")){
                    if(response.equals("successfully")){
                        Toast.makeText(Client_data.this, R.string.successfully,
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(Client_data.this, R.string.unsuccessfully,
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        change_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String request = "change weight to " + client_weight.getText().toString().trim();
                String response = work_with_server.send_get(request);
                if (!response.equals("error")){
                    if(response.equals("successfully")){
                        Toast.makeText(Client_data.this, R.string.successfully,
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(Client_data.this, R.string.unsuccessfully,
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        change_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String request = "change height to " + client_height.getText().toString().trim();
                String response = work_with_server.send_get(request);
                if (!response.equals("error")){
                    if(response.equals("successfully")){
                        Toast.makeText(Client_data.this, R.string.successfully,
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(Client_data.this, R.string.unsuccessfully,
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}