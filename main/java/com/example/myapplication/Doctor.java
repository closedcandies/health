package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Doctor extends User{
    public Integer stage;
    public Photo photo;

    public Doctor(String name, String surname, String email, String password,
                  String gender, Integer age, String date_of_birth, Integer stage) {
        super(name, surname, email, password, gender, age, date_of_birth);
    }

    public void payment(){}

}
