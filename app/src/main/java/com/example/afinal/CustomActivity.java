package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomActivity extends AppCompatActivity {


    private RecyclerView ingredientsRecyclerView;
    private TextView totalPriceText;
    private Button orderButton;
    private IngredientAdapter adapter;
    private List<Ingredient> selectedIngredients = new ArrayList<>();
    private int totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piece);

        Button BacktoMain = findViewById(R.id.button_back_custom);
        BacktoMain.setOnClickListener( v -> {
                    BacktoMain.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            BacktoMain.animate().scaleX(1f).scaleY(1f).setDuration(200);
                            Intent intent = new Intent(CustomActivity.this, MainActivity.class);
                            startActivity(intent);

                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    });
                });
        ingredientsRecyclerView = findViewById(R.id.ingredients_list);
        totalPriceText = findViewById(R.id.total_price);
        orderButton = findViewById(R.id.btn_order);

        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IngredientAdapter(getIngredients(), this::onIngredientSelected);
        ingredientsRecyclerView.setAdapter(adapter);

        orderButton.setOnClickListener(view -> placeOrder());
    }

    private List<Ingredient> getIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("Chocolate", 150));
        ingredients.add(new Ingredient("Banana", 250));
        ingredients.add(new Ingredient("Strawberry", 350));
        ingredients.add(new Ingredient("CreamCheese", 200));
        return ingredients;
    }

    private void onIngredientSelected(Ingredient ingredient) {
        selectedIngredients.add(ingredient);
        totalPrice += ingredient.getPrice();
        totalPriceText.setText("Price: " + totalPrice + " ₽");
    }

    private void placeOrder() {

    }
}




