//package com.example.afinal;
//
//public class Product {
//    private String name;
//    private int imageResId;
//    private String description;
//    private int price;
//    private String[] ingredients;
//
//    public Product(String name, int cheesecake, String description, int price, String[] ingredients) {
//        this.name = name;
//        this.imageResId = imageResId;
//        this.description = description;
//        this.price = price;
//        this.ingredients = ingredients;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public int getImageResId() {
//        return imageResId;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public int getPrice() {
//        return price;
//    }
//
//    public String[] getIngredients() {
//        return ingredients;
//    }
//}
package com.example.afinal;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable{
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(imageResId);
        dest.writeString(description);
        dest.writeInt(price);
        dest.writeStringArray(ingredients);
    }
}














