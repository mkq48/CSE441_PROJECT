package com.example.rcs;

public class Comment {
    private String userID;
    private String content;

    public Comment(int id, String userID, String content) {
        this.userID = userID;
        this.content = content;
    }
    public Comment(){}

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
