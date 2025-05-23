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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

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

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Favorites");
        }
        toolbar.setBackgroundColor(0xFF800000);
        toolbar.setTitleTextColor(0xFFF5F5DC);

        recyclerView = findViewById(R.id.recyclerFavorites);
        favoriteProducts = new ArrayList<>();

        adapter = new ProductAdapter(this, favoriteProducts, product -> {
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
            public void onRemoveFromFavorites(Product product) {
                if (auth.getCurrentUser() != null) {
                    Log.d(TAG, "Removing favorite: " + product.getName());
                    db.collection("users")
                            .document(auth.getCurrentUser().getUid())
                            .collection("favorites")
                            .document(product.getName())
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                favoriteProducts.remove(product);
                                adapter.notifyDataSetChanged();
                                ToastUtils.showCustomToast(FavoritesActivity.this, "deleted from favorites", Toast.LENGTH_SHORT);
                                if (favoriteProducts.isEmpty()) {
                                    ToastUtils.showCustomToast(FavoritesActivity.this, "favorties empty", Toast.LENGTH_SHORT);
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Failed to remove from favorites: " + e.getMessage(), e);
                                ToastUtils.showCustomToast(FavoritesActivity.this, "error removing from favorites", Toast.LENGTH_SHORT);
                            });
                } else {
                    ToastUtils.showCustomToast(FavoritesActivity.this, "login, to remove from favorties", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onAddToCart(Product product) {
                if (auth.getCurrentUser() != null) {
                    db.collection("users")
                            .document(auth.getCurrentUser().getUid())
                            .collection("cart")
                            .document(product.getName())
                            .set(product.toMap())
                            .addOnSuccessListener(aVoid -> {
                                ToastUtils.showCustomToast(FavoritesActivity.this, "added to cart", Toast.LENGTH_SHORT);
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Failed to add to cart: " + e.getMessage(), e);
                                ToastUtils.showCustomToast(FavoritesActivity.this, "error adding to cart", Toast.LENGTH_SHORT);
                            });
                } else {
                    ToastUtils.showCustomToast(FavoritesActivity.this, "login, to add to cart", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onRemoveFromCart(Product product) {
                ToastUtils.showCustomToast(FavoritesActivity.this, "delete", Toast.LENGTH_SHORT);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        if (auth.getCurrentUser() != null) {
            loadFavorites();
        } else {
            auth.signInAnonymously()
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Anonymous login successful");
                            loadFavorites();
                        } else {
                            Log.e(TAG, "Anonymous login failed: " + task.getException().getMessage(), task.getException());
                            ToastUtils.showCustomToast(this, "account error", Toast.LENGTH_SHORT);
                        }
                    });
        }
    }

    private void loadFavorites() {
        if (auth.getCurrentUser() != null) {
            db.collection("users")
                    .document(auth.getCurrentUser().getUid())
                    .collection("favorites")
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        favoriteProducts.clear();
                        for (DocumentSnapshot doc : querySnapshot) {
                            List<String> ingredientsList = (List<String>) doc.get("ingredients");
                            String[] ingredients = ingredientsList != null ?
                                    ingredientsList.toArray(new String[0]) : new String[0];
                            Product product = new Product(
                                    doc.getString("name") != null ? doc.getString("name") : "Unknown Cake",
                                    doc.getLong("imageResId") != null ? doc.getLong("imageResId").intValue() : 0,
                                    doc.getString("description") != null ? doc.getString("description") : "",
                                    doc.getLong("price") != null ? doc.getLong("price").intValue() : 0,
                                    ingredients
                            );
                            favoriteProducts.add(product);
                        }
                        adapter.notifyDataSetChanged();
                        if (favoriteProducts.isEmpty()) {
                            ToastUtils.showCustomToast(this, "favorties empty", Toast.LENGTH_SHORT);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to load favorites: " + e.getMessage(), e);
                        ToastUtils.showCustomToast(this, "error uploading favorites", Toast.LENGTH_SHORT);
                    });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}