package com.example.rcs.model;

import java.util.Map;

public class Comment {
    private String userID;
    private String content;
    private int numberOfLike;
    private String id;  //de tham chieu den comment muon like, reply
    private Map<String, Boolean> likes;
    private long timestamp;
    public Comment(String userID, String content, int numberOfLike, Map<String, Boolean> likes, long timestamp) {
        this.userID = userID;
        this.content = content;
        this.numberOfLike=numberOfLike;
        this.likes = likes;
        this.timestamp = timestamp;
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

    public Map<String, Boolean> getLikes() {
        return likes;
    }

    public void setLikes(Map<String, Boolean> likes) {
        this.likes = likes;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "userID='" + userID + '\'' +
                ", content='" + content + '\'' +
                ", numberOfLike=" + numberOfLike +
                ", id='" + id + '\'' +
                ", likes=" + likes +
                ", timestamp=" + timestamp +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
