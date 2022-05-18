package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Start_menu extends AppCompatActivity {

    private EditText user_name, user_surname, user_email, user_password,
            user_gender, user_age, user_date_of_birth;
    private Button client_reg, doctor_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

        user_name = findViewById(R.id.user_name);
        user_surname = findViewById(R.id.user_surname);
        user_email = findViewById(R.id.user_email);
        user_password = findViewById(R.id.user_password);
        user_gender = findViewById(R.id.user_gender);
        user_age = findViewById(R.id.user_age);
        client_reg = findViewById(R.id.client_reg);
        doctor_reg = findViewById(R.id.doctor_reg);
        user_date_of_birth = findViewById(R.id.user_date_of_birth);

        client_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_name.getText().toString().trim().equals("") ||
                        user_surname.getText().toString().trim().equals("") ||
                        user_email.getText().toString().trim().equals("") ||
                        user_password.getText().toString().trim().equals("") ||
                        user_gender.getText().toString().trim().equals("") ||
                        user_age.getText().toString().trim().equals("") ||
                        user_date_of_birth.getText().toString().trim().equals("")){
                    Toast.makeText(Start_menu.this, R.string.no_user_input,
                            Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(Start_menu.this, Registration.class);
                    intent.putExtra("user", user_name.getText().toString().trim() + " " +
                            user_surname.getText().toString().trim() + " " +
                            user_email.getText().toString().trim() + " " +
                            user_password.getText().toString().trim() + " " +
                            user_gender.getText().toString().trim() + " " +
                            Integer.parseInt(user_age.getText().toString().trim()) + " " +
                            user_date_of_birth.getText().toString().trim());
                    startActivity(intent);
                }
            }
        });
        doctor_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_name.getText().toString().trim().equals("") ||
                        user_surname.getText().toString().trim().equals("") ||
                        user_email.getText().toString().trim().equals("") ||
                        user_password.getText().toString().trim().equals("") ||
                        user_gender.getText().toString().trim().equals("") ||
                        user_age.getText().toString().trim().equals("")){
                    Toast.makeText(Start_menu.this, R.string.no_user_input,
                            Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(Start_menu.this, Doctor_registration.class);
                    intent.putExtra("user", user_name.getText().toString().trim() + " " +
                            user_surname.getText().toString().trim() + " " +
                            user_email.getText().toString().trim() + " " +
                            user_password.getText().toString().trim() + " " +
                            user_gender.getText().toString().trim() + " " +
                            Integer.parseInt(user_age.getText().toString().trim()) + " " +
                            user_date_of_birth.getText().toString().trim());
                    startActivity(intent);
                }
            }
        });
    }
}