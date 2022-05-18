package com.example.myapplication;

public class User {
    public String name;
    public String surname;
    private String email;
    private String password;
    public String gender;
    public Integer age;
    public String date_of_birth;

    public User(String name, String surname, String email, String password,
                String gender, Integer age, String date_of_birth){
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.age = age;
        this.date_of_birth = date_of_birth;
    }
    public void check_login(){}
    public void change_data(){}
}
