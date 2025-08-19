package com.devdroid.shoppingnew.modelclass;

public class Favorite {
    private int imageResId;
    private String price;
    private String title;
    private String weight;
    private int quantity;

    public Favorite(int imageResId, String price, int quantity, String title, String weight) {
        this.imageResId = imageResId;
        this.price = price;
        this.quantity = quantity;
        this.title = title;
        this.weight = weight;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
