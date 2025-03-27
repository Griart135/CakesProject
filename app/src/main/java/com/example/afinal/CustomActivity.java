package com.example.afinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class CustomActivity extends AppCompatActivity {
    private LinearLayout ingredientsContainer;
    private TextView totalPriceText;
    private int totalPrice = 0;
    private Button orderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piece);

        ingredientsContainer = findViewById(R.id.ingredients_container);
        totalPriceText = findViewById(R.id.total_price);
        orderButton = findViewById(R.id.btn_order);

        orderButton.setOnClickListener(v -> {
            Intent intent = new Intent(CustomActivity.this, OrderActivity.class);
            startActivity(intent);
        });

        RecyclerView ingredientsList = findViewById(R.id.ingredients_list);
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("Шоколад", 50),
                new Ingredient("Ваниль", 40),
                new Ingredient("Карамель", 60),
                new Ingredient("Шоколад", 50),
                new Ingredient("Ваниль", 40),
                new Ingredient("Карамель", 60),
                new Ingredient("Шоколад", 50),
                new Ingredient("Ваниль", 40),
                new Ingredient("Карамель", 60)
        );

        IngredientAdapter adapter = new IngredientAdapter(ingredients, this::updateCakePreview);
        ingredientsList.setAdapter(adapter);
        ingredientsList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void updateCakePreview(Ingredient ingredient, boolean isAdding) {
        if (isAdding) {
            TextView ingredientView = new TextView(this);
            ingredientView.setText(ingredient.getName());
            ingredientView.setTextSize(16);
            ingredientView.setTextColor(Color.parseColor("#F5F5DC"));
            ingredientsContainer.addView(ingredientView);
            totalPrice += ingredient.getPrice();
        } else {
            for (int i = 0; i < ingredientsContainer.getChildCount(); i++) {
                View view = ingredientsContainer.getChildAt(i);
                if (view instanceof TextView && ingredient.getName().equals(view.getTag())) {
                    ingredientsContainer.removeView(view);
                    break;
                }
            }
            totalPrice -= ingredient.getPrice();
        }
        totalPriceText.setText("Цена: " + totalPrice + " ₽");
    }
}







