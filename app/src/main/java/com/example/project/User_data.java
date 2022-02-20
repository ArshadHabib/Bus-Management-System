package com.example.project;

public class User_data {
    String full_name;
    String email;
    String user_name;
    String password;
    String dp;

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    User_data(String full_name, String email, String user_name, String password, String dp) {

        this.full_name = full_name;
        this.email = email;
        this.user_name = user_name;
        this.password = password;
        this.dp=dp;

    }

    public User_data()
    {

    }
    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }




}
