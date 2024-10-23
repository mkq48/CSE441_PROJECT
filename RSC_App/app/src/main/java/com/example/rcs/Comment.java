package com.example.rcs;

import androidx.annotation.NonNull;

public class Comment {
    private String userID;
    private String content;
    private int numberOfLike;
    private String id;
    public Comment(String userID, String content, int numberOfLike) {
        this.userID = userID;
        this.content = content;
        this.numberOfLike=numberOfLike;
    }
    public Comment(){}

    public String getUserID() {
        return userID;
    }
    public String getContent() {
        return content;
    }

    public int getNumberOfLike() {
        return numberOfLike;
    }

    public void setNumberOfLike(int numberOfLike) {
        this.numberOfLike = numberOfLike;
    }

    @NonNull
    @Override
    public String toString() {
        return "Comment{" +
                "userID='" + userID + '\'' +
                ", content='" + content + '\'' +
                ", numberOfLike=" + numberOfLike +
                ", id='" + id + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
