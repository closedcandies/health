package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Doctor_registration extends AppCompatActivity {

    private EditText doctor_stage;
    private Button doctor_reg_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);

        doctor_stage = findViewById(R.id.doctor_stage);
        doctor_reg_finish = findViewById(R.id.doctor_reg_finish);

        doctor_reg_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(doctor_stage.getText().toString().trim().equals("")){
                    Toast.makeText(Doctor_registration.this, R.string.no_user_input,
                            Toast.LENGTH_LONG).show();
                }
                else{
                    startActivity(new Intent(Doctor_registration.this, Doctor_interface.class));
                }
            }
        });
    }
}