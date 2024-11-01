package com.example.rcs;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Story {
    private String storyId;
    private String author;
    private List<String> categories;

    private String content;
    private long favorites;
    private String imageUrl;
    private String name;
    private long views;
//    private Timestamp created_at;

    public Story(int favorites, String imageUrl, String storyId, String author, String name, List<String> categories) {
        this.favorites = favorites;
        this.storyId = storyId;
        this.author = author;
        this.name = name;
        this.imageUrl = imageUrl;
        this.categories = categories;
    }

    public Story(String storyId, String name, String imageUrl) {
        this.storyId = storyId;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public Story(String storyId, String name, String imageUrl, String author) {
        this.storyId = storyId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.author = author;
    }

    public Story(String storyId, String name, String imageUrl, String author, long favorite) {
        this.storyId = storyId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.author = author;
        this.favorites = favorite;
    }

    public Story(String storyId, String name, String imageUrl,long view, String author) {
        this.storyId = storyId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.author = author;
        this.views = view;
    }

    public Story(String storyId, String author, List<String> categories, String name, String imageUrl) {
        this.storyId = storyId;
        this.author = author;
        this.categories = categories;
        this.name = name;
        this.imageUrl = imageUrl;
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

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
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

    public void setFavorites(long favorites) {
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

    public void setName(String name) {
        this.name = name;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

//    public Timestamp getCreated_at() {
//        return created_at;
//    }
//
//    public void setCreated_at(Timestamp created_at) {
//        this.created_at = created_at;
//    }
}
