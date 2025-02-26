package com.example.afinal;

import android.os.Bundle;
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
        String cakeDescription = getIntent().getStringExtra("cakeDescription");
        String[] ingredientsList = getIntent().getStringArrayExtra("ingredients");

        cakeImageView.setImageResource(imageResId);
        cakeNameTextView.setText(cakeName);

        for (String ingredient : ingredientsList) {
            TextView ingredinetTextView = new TextView(this);
            ingredinetTextView.setText(ingredient);
            ingredinetTextView.setTextSize(16);
            ingredientsLayout.addView(ingredinetTextView);
        }
    }
}

