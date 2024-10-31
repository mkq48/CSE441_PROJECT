



package com.example.rcs;

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

    public Story(String storyId, String name, String imageUrl, String author, List<String> categories) {
        this.storyId = storyId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.author = author;
        this.categories = categories;
    }


    public Story(int favorites, String imageUrl, String storyId, String name) {
        this.favorites = favorites;
        this.imageUrl = imageUrl;
        this.storyId = storyId;
        this.name = name;
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

        public int getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }
}
