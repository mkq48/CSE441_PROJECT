package com.example.rcs;

import java.util.Date;

public class User {
    private String userId, imageUrl, email, userName, password;
    private Date DoB;


    public User(String userId, String imageUrl, String email, String userName, String password, Date doB) {
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.email = email;
        this.userName = userName;
        this.password = password;
        DoB = doB;
    }

    public String getUserId() {
        return userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public Date getDoB() {
        return DoB;
    }

    public void setDoB(Date doB) {
        DoB = doB;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
