package com.example.rcs.model;

public class UserProfile {
    public String name;
    public String imageUrl;

    public UserProfile() {} // Empty constructor for Firestore

    public UserProfile(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
