package com.example.afinal;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private ArrayList<Product> cartProducts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerCart);

        cartProducts = (ArrayList<Product>) getIntent().getSerializableExtra("cart");

        if (cartProducts == null || cartProducts.isEmpty()) {
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            cartProducts = new ArrayList<>();
        }

        adapter = new ProductAdapter(this, cartProducts, product -> {

        }, new ProductAdapter.OnProductActionListener() {
            @Override
            public void onAddToFavorites(Product product) {
                Toast.makeText(CartActivity.this, "added to favorite", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAddToCart(Product product) {
                Toast.makeText(CartActivity.this, "added to cart", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
