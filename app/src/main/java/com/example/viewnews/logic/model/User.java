package com.example.viewnews.logic.model;

public class User {
    private String name;
    private String password;

    public User(){
    }

    public User(String userName, String passWord) {
        this.name = userName;
        this.password = passWord;
    }

    public String getUserName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
