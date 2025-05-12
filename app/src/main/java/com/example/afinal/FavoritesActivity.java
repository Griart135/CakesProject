package com.example.afinal;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private ArrayList<Product> favoriteProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.recyclerFavorites);

        favoriteProducts = (ArrayList<Product>) getIntent().getSerializableExtra("favorites");

        if (favoriteProducts == null || favoriteProducts.isEmpty()) {
            ToastUtils.showCustomToast(this, "Избранное пусто", Toast.LENGTH_SHORT);
            favoriteProducts = new ArrayList<>();
        }

        adapter = new ProductAdapter(this, favoriteProducts, product -> {
        }, new ProductAdapter.OnProductActionListener() {
            @Override
            public void onAddToFavorites(Product product) {
                ToastUtils.showCustomToast(FavoritesActivity.this, "Уже в избранном", Toast.LENGTH_SHORT);
            }

            @Override
            public void onAddToCart(Product product) {
                ToastUtils.showCustomToast(FavoritesActivity.this, "Добавлено в корзину", Toast.LENGTH_SHORT);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
