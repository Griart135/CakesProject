package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    private static final String TAG = "FavoritesActivity";
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private ArrayList<Product> favoriteProducts;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Set up Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Избранное");
        }
        toolbar.setBackgroundColor(0xFF800000);
        toolbar.setTitleTextColor(0xFFF5F5DC);

        recyclerView = findViewById(R.id.recyclerFavorites);

        // Safe Intent handling
        Serializable serializable = getIntent().getSerializableExtra("favorites");
        favoriteProducts = new ArrayList<>();
        if (serializable instanceof ArrayList) {
            try {
                favoriteProducts = (ArrayList<Product>) serializable;
            } catch (ClassCastException e) {
                Log.e(TAG, "Invalid favorites data: " + e.getMessage(), e);
                ToastUtils.showCustomToast(this, "error uploading favorites", Toast.LENGTH_SHORT);
            }
        }

        if (favoriteProducts.isEmpty()) {
            ToastUtils.showCustomToast(this, "favorites empty", Toast.LENGTH_SHORT);
        }

        adapter = new ProductAdapter(this, favoriteProducts, product -> {
            // Open OrderActivity on product click
            Intent intent = new Intent(FavoritesActivity.this, OrderActivity.class);
            intent.putExtra("ImageResId", product.getImageResId());
            intent.putExtra("cake_name", product.getName());
            intent.putExtra("cakeDescription", product.getDescription());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("ingredients", product.getIngredients());
            startActivity(intent);
        }, new ProductAdapter.OnProductActionListener() {
            @Override
            public void onAddToFavorites(Product product) {
                ToastUtils.showCustomToast(FavoritesActivity.this, "already in favorites", Toast.LENGTH_SHORT);
            }

            @Override
            public void onAddToCart(Product product) {
                // Save to Firestore cart
                if (auth.getCurrentUser() != null) {
                    db.collection("users")
                            .document(auth.getCurrentUser().getUid())
                            .collection("cart")
                            .document(product.getName())
                            .set(product.toMap())
                            .addOnSuccessListener(aVoid -> {
                                ToastUtils.showCustomToast(FavoritesActivity.this, "added to card", Toast.LENGTH_SHORT);
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Failed to add to cart: " + e.getMessage(), e);
                                ToastUtils.showCustomToast(FavoritesActivity.this, "error adding to card", Toast.LENGTH_SHORT);
                            });
                } else {
                    ToastUtils.showCustomToast(FavoritesActivity.this, "login to add to card", Toast.LENGTH_SHORT);
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}