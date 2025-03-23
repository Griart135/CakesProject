package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class CakeDetalsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cake_details);

        int imageResId = getIntent().getIntExtra("imageResId", -1);
        String cakeName = getIntent().getStringExtra("cakeName");
        String cakeDescription = getIntent().getStringExtra("cakeDescription");
        int price = getIntent().getIntExtra("price", -1);
        String[] ingredients = getIntent().getStringArrayExtra("ingredients");

        Log.d("CakeDetalsActivity", "imageResId: " + imageResId);
        Log.d("CakeDetalsActivity", "cakeName: " + cakeName);
        Log.d("CakeDetalsActivity", "cakeDescription: " + cakeDescription);
        Log.d("CakeDetalsActivity", "price: " + price);
        Log.d("CakeDetalsActivity", "ingredients: " + Arrays.toString(ingredients));

        if (imageResId == -1 || cakeName == null || cakeDescription == null || price == -1 || ingredients == null) {
            Log.e("CakeDetalsActivity", "Ошибка при передаче данных в активность!");
            finish();
            return;
        }

        ImageView cakeImage = findViewById(R.id.cakeImage);
        TextView cakeNameText = findViewById(R.id.cakeName);
        TextView cakeDescriptionText = findViewById(R.id.cakeDescription);
        TextView cakePrice = findViewById(R.id.cakePrice);
        TextView ingredientsText = findViewById(R.id.ingredientsText);
        Button orderButton = findViewById(R.id.btn_order);

        orderButton.setOnClickListener(v -> {
            Intent intent = new Intent(CakeDetalsActivity.this, OrderActivity.class);
            intent.putExtra("ImageResId", imageResId);
            intent.putExtra("cake_name", cakeName);
            startActivity(intent);
        });


        cakeImage.setImageResource(imageResId);
        cakeNameText.setText(cakeName);
        cakeDescriptionText.setText(cakeDescription);
        cakePrice.setText(String.format("Цена: %d ₽", price));

        StringBuilder ingredientsList = new StringBuilder();
        if (ingredients != null && ingredients.length > 0) {
            for (String ingredient : ingredients) {
                ingredientsList.append(ingredient).append("\n");
            }
        } else {
            ingredientsList.append("Ингредиенты не указаны.");
        }
        ingredientsText.setText(ingredientsList.toString());
    }
}



