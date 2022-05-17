package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Client_interface extends AppCompatActivity {

    private Button change_data, order_consultation;
    private EditText find_by_stage;
    private Work_with_server work_with_server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_interface);

        change_data = findViewById(R.id.change_data);
        order_consultation = findViewById(R.id.order_consultation);
        find_by_stage = findViewById(R.id.find_by_stage);

        change_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Client_interface.this, Client_data.class);
                startActivity(intent);
            }
        });
        order_consultation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(find_by_stage.getText().toString().trim().equals("")){
                    Toast.makeText(Client_interface.this, R.string.no_user_input,
                            Toast.LENGTH_LONG).show();
                }
                else{
                    String request = "consultation " + find_by_stage;
                    String response = work_with_server.send_get(request);
                    if (!response.equals("error")){
                        if(response.equals("has_doctor")){
                            Toast.makeText(Client_interface.this, R.string.has_doctor,
                                    Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(Client_interface.this, R.string.no_doctor,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }
}