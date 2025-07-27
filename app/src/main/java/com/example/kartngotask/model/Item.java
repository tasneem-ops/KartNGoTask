package com.example.kartngotask.model;

public class Item {
    private String title;
    private String imageUri;
    private double price;

    // No-argument constructor required for Firestore
    public Item() {
    }

    public Item(String title, String imageUri, double price) {
        this.title = title;
        this.imageUri = imageUri;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}