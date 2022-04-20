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
    private Button finish_check;
    private Data_base data_base;

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
        finish_check = findViewById(R.id.finish_check);

        finish_check.setOnClickListener(new View.OnClickListener() {
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
//                    if(data_base.get_from_data_base(new User(check_user_name.getText().toString().trim(),
//                            check_user_surname.getText().toString().trim(),
//                            check_user_email.getText().toString().trim(),
//                            check_user_password.getText().toString().trim(),
//                            check_user_gender.getText().toString().trim(),
//                            Integer.parseInt(check_user_age.getText().toString().trim()),
//                            check_user_date_of_birth.getText().toString().trim()))){
//                        startActivity(new Intent(Enter_to_system.this, *Реализовать переход на оба интерфейса*.class));
//                    } *В дальнейшем удалить строку снизу*
                    startActivity(new Intent(Enter_to_system.this, Client_interface.class));
                }
            }
        });
    }
}