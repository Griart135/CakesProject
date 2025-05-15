package com.example.afinal;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Product implements Parcelable {
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

    protected Product(Parcel in) {
        name = in.readString();
        imageResId = in.readInt();
        description = in.readString();
        price = in.readInt();
        ingredients = in.createStringArray();
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public Product(String id, String s, int i, String s1, int i1, String[] ingredients) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    public Map<String, Object> toMap() {
        Map<String,Object> map = new HashMap<>();
        map.put("name", name != null ? name : "Unknown cake");
        map.put("imageResId", imageResId);
        map.put("description", description != null ? description : " ");
        map.put("price", price);
        map.put("ingredients", ingredients != null ? Arrays.asList(ingredients) : new ArrayList<>());
        return map;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(imageResId);
        dest.writeString(description);
        dest.writeInt(price);
        dest.writeStringArray(ingredients);
    }
}














