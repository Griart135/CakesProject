package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CakeDetalsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cake_details);

        ImageView cakeImageView = findViewById(R.id.banana_for_pager);
        TextView cakeNameTextView = findViewById(R.id.cake_name);
        LinearLayout ingredientsLayout = findViewById(R.id.ingredients_layout);

        int imageResId = getIntent().getIntExtra("imageResId", -1);
        String cakeName = getIntent().getStringExtra("cakeName");
        String[] ingredientsList = getIntent().getStringArrayExtra("ingredients");

        if (imageResId != -1) {
            cakeImageView.setImageResource(imageResId);
        }
        cakeNameTextView.setText(cakeName);

        if (ingredientsList != null) {
            for (String ingredient : ingredientsList) {
                TextView ingredientTextView = new TextView(this);
                ingredientTextView.setText(ingredient);
                ingredientTextView.setTextSize(16);
                ingredientsLayout.addView(ingredientTextView);
            }
        }

        Button orderButton = findViewById(R.id.order_button);

        orderButton.setOnClickListener(v -> {
            Intent intent = new Intent(CakeDetalsActivity.this, OrderActivity.class);
            intent.putExtra("imageResId", imageResId);
            intent.putExtra("cakeName", cakeName);
            intent.putExtra("cakePrice", "Price: 1200₽");
            startActivity(intent);
        });

    }
}


