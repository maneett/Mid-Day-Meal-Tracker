package com.example.middaymealtracker;

public class user {
    public String fullname, email, age , password;

    public user() {
    }

    public user(String fullname, String email, String age, String password){
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.age = age;
    }
}
