package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomActivity extends AppCompatActivity {

    private RecyclerView ingredientsRecyclerView;
    private TextView totalPriceText;
    private Button orderButton;
    private IngredientAdapter adapter;
    private Set<Ingredient> selectedIngredients = new HashSet<>();
    private int totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piece);

        Button BacktoMain = findViewById(R.id.button_back_custom);
        BacktoMain.setOnClickListener(v -> {
            Intent intent = new Intent(CustomActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        ingredientsRecyclerView = findViewById(R.id.ingredients_list);
        totalPriceText = findViewById(R.id.total_price);
        orderButton = findViewById(R.id.btn_order);

        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IngredientAdapter(getIngredients(), this::updateIngredientSelection);
        ingredientsRecyclerView.setAdapter(adapter);

        orderButton.setOnClickListener(view -> placeOrder());
    }

    private List<Ingredient> getIngredients() {
        return List.of(
                new Ingredient("Chocolate", 150),
                new Ingredient("Banana", 250),
                new Ingredient("Strawberry", 350),
                new Ingredient("CreamCheese", 200)
        );
    }

    private void updateIngredientSelection(Ingredient ingredient, boolean isAdding) {
        if (isAdding) {
            selectedIngredients.add(ingredient);
            totalPrice += ingredient.getPrice();
        } else {
            selectedIngredients.remove(ingredient);
            totalPrice -= ingredient.getPrice();
        }

        totalPriceText.setText("Price: " + totalPrice + " ₽");
    }

    private void placeOrder() {
    }
}





