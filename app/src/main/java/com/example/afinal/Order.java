package com.example.afinal;

import java.util.List;

public class Order {
    private String name;
    private String description;
    private int price;
    private List<String> ingredients;
    private int height;
    private int radius;
    private int slices;
    private String address;
    private String userId;

    public Order() {}

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getPrice() { return price; }
    public List<String> getIngredients() { return ingredients; }
    public int getHeight() { return height; }
    public int getRadius() { return radius; }
    public int getSlices() { return slices; }
    public String getAddress() { return address; }
    public String getUserId() { return userId; }
}
