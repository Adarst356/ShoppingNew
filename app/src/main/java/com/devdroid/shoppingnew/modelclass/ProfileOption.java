package com.devdroid.shoppingnew.modelclass;

public class ProfileOption {
    private int iconRes;
    private String title;

    public ProfileOption(int iconRes, String title) {
        this.iconRes = iconRes;
        this.title = title;
    }

    public int getIconRes() {
        return iconRes;
    }

    public String getTitle() {
        return title;
    }
}