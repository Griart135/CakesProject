package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        String categoryName = getIntent().getStringExtra("categoryName");

        TextView categoryTitle = findViewById(R.id.categoryTitle);
        categoryTitle.setText(categoryName);

        RecyclerView productGrid = findViewById(R.id.productGrid);
        productGrid.setLayoutManager(new GridLayoutManager(this, 2));

        List<Product> products = new ArrayList<>();
        if ("cakes".equals(categoryName)) {
            products.add(new Product("Chocolate Cake", R.drawable.cheesecake, "Шоколадный торт", 500,
                    new String[] {"Шоколад", "Мука", "Сахар"}));
            products.add(new Product("Strawberry Cake", R.drawable.cheesecake, "Клубничный торт", 550,
                    new String[] {"Клубника", "Мука", "Сахар"}));
        } else if ("other".equals(categoryName)) {
            products.add(new Product("Banana Cake", R.drawable.cheesecake, "Банановый торт", 520,
                    new String[] {"Бананы", "Мука", "Сахар"}));
            products.add(new Product("Vanilla Cake", R.drawable.cheesecake, "Ванильный торт", 480,
                    new String[] {"Ваниль", "Мука", "Сахар"}));
        }

        productAdapter = new ProductAdapter(this, products, this::openProductDetails);
        productGrid.setAdapter(productAdapter);
    }

    private void openProductDetails(Product product) {
        Intent intent = new Intent(this, CakeDetalsActivity.class);
        intent.putExtra("imageResId", product.getImageResId());
        intent.putExtra("cakeName", product.getName());
        intent.putExtra("cakeDescription", product.getDescription());
        intent.putExtra("price", product.getPrice());
        intent.putExtra("ingredients", product.getIngredients());
        startActivity(intent);
    }
}


