package com.example.afinal;

import android.annotation.SuppressLint;
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

public class CartActivity extends AppCompatActivity {

    private static final String TAG = "CartActivity";
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private ArrayList<Product> cartProducts;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Wishlist");
        }
        toolbar.setBackgroundColor(0xFF800000);
        toolbar.setTitleTextColor(0xFFF5F5DC);

        recyclerView = findViewById(R.id.recyclerCart);
        cartProducts = new ArrayList<>();

        adapter = new ProductAdapter(this, cartProducts, product -> {
            Intent intent = new Intent(CartActivity.this, OrderActivity.class);
            intent.putExtra("ImageResId", product.getImageResId());
            intent.putExtra("cake_name", product.getName());
            intent.putExtra("cakeDescription", product.getDescription());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("ingredients", product.getIngredients());
            startActivity(intent);
        }, new ProductAdapter.OnProductActionListener() {
            @Override
            public void onAddToFavorites(Product product) {
                if (auth.getCurrentUser() != null) {
                    Log.d(TAG, "Adding to favorites: " + product.getName());
                    db.collection("users")
                            .document(auth.getCurrentUser().getUid())
                            .collection("favorites")
                            .document(product.getName())
                            .set(product.toMap())
                            .addOnSuccessListener(aVoid -> {
                                ToastUtils.showCustomToast(CartActivity.this, "added to favorites", Toast.LENGTH_SHORT);
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Failed to add to favorites: " + e.getMessage(), e);
                                ToastUtils.showCustomToast(CartActivity.this, "error adding to favorites", Toast.LENGTH_SHORT);
                            });
                } else {
                    ToastUtils.showCustomToast(CartActivity.this, "login, to add to favorites", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onRemoveFromFavorites(Product product) {
                ToastUtils.showCustomToast(CartActivity.this, "delete", Toast.LENGTH_SHORT);
            }

            @Override
            public void onAddToCart(Product product) {
                ToastUtils.showCustomToast(CartActivity.this, "already in cart", Toast.LENGTH_SHORT);
            }

            @Override
            public void onRemoveFromCart(Product product) {
                if (auth.getCurrentUser() != null) {
                    Log.d(TAG, "Removing from cart: " + product.getName());
                    db.collection("users")
                            .document(auth.getCurrentUser().getUid())
                            .collection("cart")
                            .document(product.getName())
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                cartProducts.remove(product);
                                adapter.notifyDataSetChanged();
                                ToastUtils.showCustomToast(CartActivity.this, "deleted from cart", Toast.LENGTH_SHORT);
                                if (cartProducts.isEmpty()) {
                                    ToastUtils.showCustomToast(CartActivity.this, "cart empty", Toast.LENGTH_SHORT);
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Failed to remove from cart: " + e.getMessage(), e);
                                ToastUtils.showCustomToast(CartActivity.this, "error deleting from cart", Toast.LENGTH_SHORT);
                            });
                } else {
                    ToastUtils.showCustomToast(CartActivity.this, "login, to delete from cart", Toast.LENGTH_SHORT);
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        if (auth.getCurrentUser() != null) {
            loadCart();
        } else {
            auth.signInAnonymously()
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Anonymous login successful");
                            loadCart();
                        } else {
                            Log.e(TAG, "Anonymous login failed: " + task.getException().getMessage(), task.getException());
                            ToastUtils.showCustomToast(this, "account error", Toast.LENGTH_SHORT);
                        }
                    });
        }
    }

    private void loadCart() {
        if (auth.getCurrentUser() != null) {
            db.collection("users")
                    .document(auth.getCurrentUser().getUid())
                    .collection("cart")
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        cartProducts.clear();
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
                            cartProducts.add(product);
                        }
                        adapter.notifyDataSetChanged();
                        if (cartProducts.isEmpty()) {
                            ToastUtils.showCustomToast(this, "empty cart", Toast.LENGTH_SHORT);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to load cart: " + e.getMessage(), e);
                        ToastUtils.showCustomToast(this, "error uploading cart", Toast.LENGTH_SHORT);
                    });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}