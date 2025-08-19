package com.devdroid.shoppingnew.modelclass;

import java.util.List;

public class Product {
    private int id;
    private String title;
    private double price;
    private String description;
    private List<String> images;

    public int getId() { return id; }
    public String getTitle() { return title; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public List<String> getImages() { return images; }
}
