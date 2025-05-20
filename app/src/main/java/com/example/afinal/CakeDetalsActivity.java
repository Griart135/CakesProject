package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Arrays;

public class CakeDetalsActivity extends AppCompatActivity {

    private ImageView cakeImage;
    private TextView cakeNameText;
    private TextView cakeDescriptionText;
    private TextView cakePrice;
    private TextView ingredientsText;
    private TextView deliveryTime;
    private Button btnOrder;
    private Button btnCakeInfo;
    private Button btnAddToFavorites;
    private boolean isFavorite = false;

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

        cakeImage = findViewById(R.id.cakeImage);
        cakeNameText = findViewById(R.id.cakeName);
        cakeDescriptionText = findViewById(R.id.cakeDescription);
        cakePrice = findViewById(R.id.cakePrice);
        ingredientsText = findViewById(R.id.ingredientsText);
        deliveryTime = findViewById(R.id.deliveryTime);
        btnOrder = findViewById(R.id.btn_order);
        btnCakeInfo = findViewById(R.id.btn_cake_info);

        setupCakeData(imageResId, cakeName, cakeDescription, price, ingredients);

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CakeDetalsActivity.this, OrderActivity.class);
                intent.putExtra("ImageResId", imageResId);
                intent.putExtra("cake_name", cakeName);
                startActivity(intent);
                deliveryTime.setText("Order placed! Ready in 45 min");
            }
        });

        btnCakeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CakeDetalsActivity", "Cake Info button clicked");
                CakeInfoDialogFragment dialog = new CakeInfoDialogFragment();
                Bundle args = new Bundle();
                args.putString("cakeName", cakeName);
                args.putStringArray("ingredients", ingredients);
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "CakeInfoDialog");
            }
        });
    }

    private void setupCakeData(int imageResId, String cakeName, String cakeDescription, int price, String[] ingredients) {
        cakeImage.setImageResource(imageResId);
        cakeNameText.setText(cakeName);
        cakeDescriptionText.setText(cakeDescription);
        cakePrice.setText(String.format("Price: %d ₽", price));

        StringBuilder ingredientsList = new StringBuilder();
        if (ingredients != null && ingredients.length > 0) {
            for (String ingredient : ingredients) {
                ingredientsList.append(ingredient).append("\n");
            }
        } else {
            ingredientsList.append("Ингредиенты не указаны.");
        }
        ingredientsText.setText(ingredientsList.toString());

        deliveryTime.setText("Ready in 45 min");
    }
}


