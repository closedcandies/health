package com.example.myapplication;

public class Doctor extends User{
    public Integer stage;

    public Doctor(String name, String surname, String email, String password,
                  String gender, Integer age, String date_of_birth, Integer stage) {
        super(name, surname, email, password, gender, age, date_of_birth);
    }

    public void payment(){}

}
