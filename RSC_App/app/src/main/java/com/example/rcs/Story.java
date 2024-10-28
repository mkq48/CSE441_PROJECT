package com.example.rcs;
import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Story {
    private String storyId;
    private String author;
    private String categories;
    private String content;
    private int favorites;
    private String imageUrl;
    private String name;
    private int views;

    public long getViews() {
        return views;
    }

    public Story(int favorites, String imageUrl, String storyId, String author, String name) {
        this.favorites = favorites;
        this.storyId = storyId;
        this.author = author;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public void setViews(int views) {
        this.views = views;
    }


    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public Story(String storyId, String name, String imageUrl) {
        this.storyId = storyId;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Story{" +
                "storyId='" + storyId + '\''+
                ", name='" + name+
                '}';
    }
}
