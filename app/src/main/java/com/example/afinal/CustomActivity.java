package com.example.afinal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CustomActivity extends AppCompatActivity {
    private LinearLayout ingredientsContainer;
    private TextView totalPriceText;
    private int totalPrice = 0;
    private Button orderButton;
    private RecyclerView cakesRecyclerView;
    private CakeAdapter cakeAdapter;

    private List<String> selectedIngredients = new ArrayList<>();
    private List<Cake> cakeList = Arrays.asList(
            new Cake("Шоколадный торт", R.drawable.nostalgy, Arrays.asList("Шоколад", "Мука", "Сахар")),
            new Cake("Ванильный торт", R.drawable.cheesecake, Arrays.asList("Ваниль", "Мука", "Сахар")),
            new Cake("Карамельный торт", R.drawable.choco_straw1, Arrays.asList("Карамель", "Мука", "Сахар")),
            new Cake("Медовый торт", R.drawable.nostalgy, Arrays.asList("Мед", "Мука", "Сахар")),
            new Cake("Ягодный торт", R.drawable.cheesecake, Arrays.asList("Ягоды", "Мука", "Сахар")),
            new Cake("Обычный торт", R.drawable.choco_straw1, Arrays.asList("Мед", "Карамель", "Сахар", "Хориз", "Мука", "Шоколад", "Ваниль"))
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piece);

        ingredientsContainer = findViewById(R.id.ingredients_container);
        totalPriceText = findViewById(R.id.total_price);
        orderButton = findViewById(R.id.btn_order);
        cakesRecyclerView = findViewById(R.id.cakes_recycler_view);

        if (ingredientsContainer == null || totalPriceText == null || orderButton == null || cakesRecyclerView == null) {
            Log.e("CustomActivity", "One or more views are null");
            return;
        }

        orderButton.setOnClickListener(v -> {
            Intent intent = new Intent(CustomActivity.this, OrderActivity.class);
            startActivity(intent);
        });

        RecyclerView ingredientsList = findViewById(R.id.ingredients_list);
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("Шоколад", 50),
                new Ingredient("Ваниль", 40),
                new Ingredient("Карамель", 60),
                new Ingredient("Сахар", 50),
                new Ingredient("Хориз", 40),
                new Ingredient("Мед", 60)
        );

        IngredientAdapter adapter = new IngredientAdapter(ingredients, this::updateCakePreview);
        ingredientsList.setAdapter(adapter);
        ingredientsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        cakeAdapter = new CakeAdapter(cakeList);
        cakesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        cakesRecyclerView.setAdapter(cakeAdapter);

        filterCakesByIngredients();
    }

    private void updateCakePreview(Ingredient ingredient, boolean isAdding) {
        Log.d("CustomActivity", "Updating preview: " + ingredient.getName() + ", isAdding: " + isAdding);
        if (isAdding) {
            selectedIngredients.add(ingredient.getName());
            TextView ingredientView = new TextView(this);
            ingredientView.setText(ingredient.getName());
            ingredientView.setTextSize(16);
            ingredientView.setTextColor(Color.parseColor("#F5F5DC"));
            ingredientView.setPadding(8, 4, 8, 4);
            ingredientsContainer.addView(ingredientView);
            totalPrice += ingredient.getPrice();
        } else {
            selectedIngredients.remove(ingredient.getName());
            for (int i = 0; i < ingredientsContainer.getChildCount(); i++) {
                View view = ingredientsContainer.getChildAt(i);
                if (view instanceof TextView && ingredient.getName().equals(((TextView) view).getText().toString())) {
                    ingredientsContainer.removeView(view);
                    break;
                }
            }
            totalPrice -= ingredient.getPrice();
        }
        totalPriceText.setText("Цена: " + totalPrice + " ₽");
        Log.d("CustomActivity", "Selected ingredients: " + selectedIngredients);

        filterCakesByIngredients();
    }

    private void filterCakesByIngredients() {
        List<Cake> filteredCakes = cakeList.stream()
                .filter(cake -> selectedIngredients.isEmpty() ||
                        selectedIngredients.stream().allMatch(ingredient -> cake.getIngredients().contains(ingredient)))
                .collect(Collectors.toList());
        Log.d("CustomActivity", "Filtered cakes count: " + filteredCakes.size() + ", cakes: " + filteredCakes);
        cakeAdapter.updateCakes(filteredCakes);

        if (filteredCakes.isEmpty()) {
            totalPriceText.setText("Нет подходящих тортов");
        }
    }
}

















