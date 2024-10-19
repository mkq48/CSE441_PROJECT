package com.example.rcs;

public class User {
    private String userId;
    private String imageUrl;
    private String userName;
    private String passWord;
    private String email;

    public String getCurrentUserId() {
        return "user1";
    }
    public User getUserById(String id){

        return new User();
    }
    public User getCurrentUser(){
        return new User();
    }
    public void addNewUser(){

    }

    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }


    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }


    public String getEmail() {
        return email;
    }
    public void UpdateCurrentUser(){

    }
}
