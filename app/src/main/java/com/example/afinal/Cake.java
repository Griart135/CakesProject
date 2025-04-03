package com.example.afinal;

import java.util.List;

public class Cake {
    private String name;
    private int price;
    private List<String> ingredients;
    private int imageResId;

    public Cake(String name, int imageResId, List<String> ingredients) {
        this.name = name;
        this.imageResId = imageResId;
        this.ingredients = ingredients;
    }

    public Cake(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public int getImageResId() {
        return imageResId;
    }
}
