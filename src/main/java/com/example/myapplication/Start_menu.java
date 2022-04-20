package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Start_menu extends AppCompatActivity {

    private Button enter_to_system, start_reg;
    private Data_base data_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

        enter_to_system = findViewById(R.id.enter_to_system);
        start_reg = findViewById(R.id.start_reg);

        enter_to_system.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Start_menu.this, Enter_to_system.class));
            }
        });
        start_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Start_menu.this, MainActivity.class));
            }
        });
    }
}