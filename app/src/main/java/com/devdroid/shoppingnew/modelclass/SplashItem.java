package com.devdroid.shoppingnew.modelclass;

public class SplashItem {
    private int imageRes;
    private String title;
    private String description;

    public SplashItem(int imageRes, String title, String description) {
        this.imageRes = imageRes;
        this.title = title;
        this.description = description;
    }

    public int getImageRes() {
        return imageRes;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
