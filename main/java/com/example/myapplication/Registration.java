package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registration extends AppCompatActivity {

    private EditText client_weight, client_height;
    private Button client_reg_finish;
    private Work_with_server work_with_server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        client_weight = findViewById(R.id.client_weight);
        client_height = findViewById(R.id.client_height);
        client_reg_finish = findViewById(R.id.client_reg_finish);

        client_reg_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(client_weight.getText().toString().trim().equals("") ||
                        client_height.getText().toString().trim().equals("")){
                    Toast.makeText(Registration.this, R.string.no_user_input,
                            Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = getIntent();
                    //ниже получаем строчку вида: client_registration <параметры юзера> <праметры клиента(
                    // вес, рост)>. Тебе надо будет обрабатывать эту строку при запросе к бд.
                    String request = "client_registration " + intent.getStringExtra("user") + " " +
                            client_weight.getText().toString().trim() + " " +
                            client_height.getText().toString().trim();
                    //далее ответ от твоего сервера в виде строки "not same user" или "has same user"
                    String response = work_with_server.send_get(request);
                    if (!response.equals("error")){
                        if(response.equals("not same user")){
                            Intent intent1 = new Intent(Registration.this, Client_interface.class);
                            intent1.putExtra("client", request);
                            startActivity(intent1);
                        }
                        else {
                            Toast.makeText(Registration.this, R.string.has_same_user,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }
}