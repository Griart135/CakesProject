package com.example.afinal;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 bannerSlider = findViewById(R.id.bannerSlider);
        int[] bannerImages = {R.drawable.choco_straw1, R.drawable.nostalgy, R.drawable.choco_straw1, R.drawable.nostalgy,
                R.drawable.choco_straw1, R.drawable.nostalgy, R.drawable.choco_straw1, R.drawable.nostalgy,
                R.drawable.choco_straw1, R.drawable.nostalgy, R.drawable.choco_straw1, R.drawable.nostalgy};
        BannerAdapter bannerAdapter = new BannerAdapter(this, bannerImages);
        bannerSlider.setAdapter(bannerAdapter);

        TextView category1 = findViewById(R.id.category1);
        TextView category2 = findViewById(R.id.category2);

        category1.setOnClickListener(v -> openCategory("cakes"));
        category2.setOnClickListener(v -> openCategory("other"));

        RecyclerView productGrid = findViewById(R.id.productGrid);
        productGrid.setLayoutManager(new GridLayoutManager(this, 2));

        List<Product> products = new ArrayList<>();
        products.add(new Product(
                "Chocolate Cake", R.drawable.choco_straw1, "Шоколадный торт", 500,
                new String[] {"Шоколад ", "Какао", "Сливки "}
        ));
        products.add(new Product("Strawberry Cake", R.drawable.straw1, "Клубничный торт", 550,
                new String[] {"Клубника", "Сахар", "Творожный сыр"}
        ));
        products.add(new Product("Banana Cake", R.drawable.banana4, "Банановый торт", 520,
                new String[] {"Банан", "Шоколад ", "Крем"}
        ));
        products.add(new Product("Vanilla Cake", R.drawable.nostalgy, "Ванильный торт", 480,
                new String[] {"Ваниль", "Мука ", "Масло"}
        ));

        ProductAdapter productAdapter = new ProductAdapter(this, products, this::openProductDetails);
        productGrid.setAdapter(productAdapter);

        ImageView iconGoToCustom = findViewById(R.id.iconGoToCustom);
        iconGoToCustom.setOnClickListener(v -> openCustomActivity());
    }

    private void openCategory(String categoryName) {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("categoryName", categoryName);
        startActivity(intent);
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

    private void openCustomActivity() {
        Intent intent = new Intent(this, CustomActivity.class);
        startActivity(intent);
    }
}








