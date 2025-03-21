package com.example.afinal;

public class Product {
    private String name;
    private int imageResId;
    private String description;
    private int price;
    private String[] ingredients;

    public Product(String name, int imageResId, String description, int price, String[] ingredients) {
        this.name = name;
        this.imageResId = imageResId;
        this.description = description;
        this.price = price;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public String[] getIngredients() {
        return ingredients;
    }
}


