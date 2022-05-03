package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Enter_to_system extends AppCompatActivity {

    private EditText check_user_name, check_user_surname, check_user_email, check_user_password,
            check_user_gender, check_user_age, check_user_date_of_birth;
    private Button client_finish_check, doctor_finish_check;
    private Work_with_server work_with_server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_to_system);

        check_user_name = findViewById(R.id.check_user_name);
        check_user_surname = findViewById(R.id.check_user_surname);
        check_user_email = findViewById(R.id.check_user_email);
        check_user_password = findViewById(R.id.check_user_password);
        check_user_gender = findViewById(R.id.check_user_gender);
        check_user_age = findViewById(R.id.check_user_age);
        check_user_date_of_birth = findViewById(R.id.check_user_date_of_birth);
        client_finish_check = findViewById(R.id.client_finish_check);
        doctor_finish_check = findViewById(R.id.doctor_finish_check);

        client_finish_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_user_name.getText().toString().trim().equals("") ||
                        check_user_surname.getText().toString().trim().equals("") ||
                        check_user_email.getText().toString().trim().equals("") ||
                        check_user_password.getText().toString().trim().equals("") ||
                        check_user_gender.getText().toString().trim().equals("") ||
                        check_user_age.getText().toString().trim().equals("") ||
                        check_user_date_of_birth.getText().toString().trim().equals("")){
                    Toast.makeText(Enter_to_system.this, R.string.no_user_input,
                            Toast.LENGTH_LONG).show();
                }
                else{
                    //отличие от Registration в том, что начинается не с "registration", а с "enter"
                    //и в отсутствии персональных параметров, так как и рост и вес и стаж работы
                    //могут менятся со временем. Да и вообще проверка по этим параметрам не должна осуществляться.
                    String request = new User(check_user_name.getText().toString().trim(),
                            check_user_surname.getText().toString().trim(),
                            check_user_email.getText().toString().trim(),
                            check_user_password.getText().toString().trim(),
                            check_user_gender.getText().toString().trim(),
                            Integer.parseInt(check_user_age.getText().toString().trim()),
                            check_user_date_of_birth.getText().toString().trim()).toString();
                    String response = work_with_server.send_get("client_enter" + request);
                    if (!response.equals("error")){
                        if(response.equals("has same user")){
                            Intent intent = new Intent(Enter_to_system.this, Client_interface.class);
                            intent.putExtra("client", request);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(Enter_to_system.this, R.string.no_same_user,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
        doctor_finish_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_user_name.getText().toString().trim().equals("") ||
                        check_user_surname.getText().toString().trim().equals("") ||
                        check_user_email.getText().toString().trim().equals("") ||
                        check_user_password.getText().toString().trim().equals("") ||
                        check_user_gender.getText().toString().trim().equals("") ||
                        check_user_age.getText().toString().trim().equals("") ||
                        check_user_date_of_birth.getText().toString().trim().equals("")){
                    Toast.makeText(Enter_to_system.this, R.string.no_user_input,
                            Toast.LENGTH_LONG).show();
                }
                else{
                    String request = new User(check_user_name.getText().toString().trim(),
                            check_user_surname.getText().toString().trim(),
                            check_user_email.getText().toString().trim(),
                            check_user_password.getText().toString().trim(),
                            check_user_gender.getText().toString().trim(),
                            Integer.parseInt(check_user_age.getText().toString().trim()),
                            check_user_date_of_birth.getText().toString().trim()).toString();
                    String response = work_with_server.send_get("doctor_enter" + request);
                    if (!response.equals("error")){
                        if(response.equals("has same user")){
                            Intent intent = new Intent(Enter_to_system.this, Doctor_interface.class);
                            intent.putExtra("doctor", request);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(Enter_to_system.this, R.string.no_same_user,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }
}