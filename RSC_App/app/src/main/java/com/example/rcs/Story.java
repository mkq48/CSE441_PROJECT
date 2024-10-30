package com.example.rcs;
import androidx.annotation.NonNull;

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
    private int favorites;
    private String imageUrl;
    private String name;
    private int views;

    public long getViews() {
        return views;


    }
//    public Story(String storyId, String author, List<String> categories, String name, String imageUrl) {
//        this.storyId = storyId;
//        this.author = author;
//        this.categories = categories;
//        this.name = name;
//        this.imageUrl = imageUrl;
//    }


    public Story(int favorites, String imageUrl, String storyId,  String name) {
        this.favorites = favorites;
          this.imageUrl = imageUrl;
        this.storyId = storyId;
        this.name = name;


    }
//    public Story(String storyId, String name, String imageUrl, List<String> categories) {
//        this.storyId = storyId;
//        this.name = name;
//        this.imageUrl = imageUrl;
//        this.categories = categories;
//    }


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


    public Story(String storyId, String name, String imageUrl ) {
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
