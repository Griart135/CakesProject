package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView categoryChocolate, categoryHoney, categoryBerry, categoryCaramel, categoryVanilla;
    private ImageView iconFavorite, iconCart, iconShare, iconProfile;
    private EditText searchBar;
    private RecyclerView productGrid;
    private ProductAdapter productAdapter;
    private List<Product> allProducts = new ArrayList<>();
    private List<Product> favoriteCakes = new ArrayList<>();
    private List<Product> cartCakes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ImageView menuIcon = findViewById(R.id.menu_icon);
        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                ToastUtils.showCustomToast(this, "main", Toast.LENGTH_SHORT);
            } else if (id == R.id.nav_custom) {
                startActivity(new Intent(this, CustomActivity.class));
            } else if (id == R.id.nav_profile) {
                ToastUtils.showCustomToast(this, "profile", Toast.LENGTH_SHORT);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        initViews();
        setupProductList();

        productAdapter = new ProductAdapter(this, allProducts, this::openProductDetails, new ProductAdapter.OnProductActionListener() {
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
                                if (!favoriteCakes.contains(product)) {
                                    favoriteCakes.add(product);
                                }
                                ToastUtils.showCustomToast(MainActivity.this, "added to favorites", Toast.LENGTH_SHORT);
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Failed to add to favorites: " + e.getMessage(), e);
                                ToastUtils.showCustomToast(MainActivity.this, "error adding to favorites", Toast.LENGTH_SHORT);
                            });
                } else {
                    ToastUtils.showCustomToast(MainActivity.this, "login, to save to favorites", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onRemoveFromFavorites(Product product) {
                if (auth.getCurrentUser() != null) {
                    Log.d(TAG, "Removing from favorites: " + product.getName());
                    db.collection("users")
                            .document(auth.getCurrentUser().getUid())
                            .collection("favorites")
                            .document(product.getName())
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                favoriteCakes.remove(product);
                                ToastUtils.showCustomToast(MainActivity.this, "deleted from favorites", Toast.LENGTH_SHORT);
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Failed to remove from favorites: " + e.getMessage(), e);
                                ToastUtils.showCustomToast(MainActivity.this, "error delelting from favorites", Toast.LENGTH_SHORT);
                            });
                } else {
                    ToastUtils.showCustomToast(MainActivity.this, "login, to delete from favorites", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onAddToCart(Product product) {
                if (auth.getCurrentUser() != null) {
                    Log.d(TAG, "Adding to cart: " + product.getName());
                    db.collection("users")
                            .document(auth.getCurrentUser().getUid())
                            .collection("cart")
                            .document(product.getName())
                            .set(product.toMap())
                            .addOnSuccessListener(aVoid -> {
                                if (!cartCakes.contains(product)) {
                                    cartCakes.add(product);
                                }
                                ToastUtils.showCustomToast(MainActivity.this, "added to cart", Toast.LENGTH_SHORT);
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Failed to add to cart: " + e.getMessage(), e);
                                ToastUtils.showCustomToast(MainActivity.this, "error adding to cart", Toast.LENGTH_SHORT);
                            });
                } else {
                    ToastUtils.showCustomToast(MainActivity.this, "login, to add to cart", Toast.LENGTH_SHORT);
                }
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
                                cartCakes.remove(product);
                                ToastUtils.showCustomToast(MainActivity.this, "deleted from cart", Toast.LENGTH_SHORT);
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Failed to remove from cart: " + e.getMessage(), e);
                                ToastUtils.showCustomToast(MainActivity.this, "error deleting from cart", Toast.LENGTH_SHORT);
                            });
                } else {
                    ToastUtils.showCustomToast(MainActivity.this, "login to delete from cart", Toast.LENGTH_SHORT);
                }
            }
        });
        productGrid.setLayoutManager(new LinearLayoutManager(this));
        productGrid.setAdapter(productAdapter);

        setupClickListeners();

        if (auth.getCurrentUser() != null) {
            loadFavorites();
            loadCart();
        } else {
            auth.signInAnonymously()
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Anonymous login successful");
                            loadFavorites();
                            loadCart();
                        } else {
                            Log.e(TAG, "Anonymous login failed: " + task.getException().getMessage(), task.getException());
                            ToastUtils.showCustomToast(this, "account error", Toast.LENGTH_SHORT);
                        }
                    });
        }

        db.collection("cakes").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                task.getResult().forEach(document -> {
                    String name = document.getString("name");
                    Long price = document.getLong("price");
                    Log.d(TAG, "Document: " + name + ", Price: " + price);
                });
            } else {
                Log.e(TAG, "Error getting cakes", task.getException());
            }
        });
    }

    private void initViews() {
        categoryChocolate = findViewById(R.id.categoryChocolate);
        categoryHoney = findViewById(R.id.categoryHoney);
        categoryBerry = findViewById(R.id.categoryBerry);
        categoryCaramel = findViewById(R.id.categoryCaramel);
        categoryVanilla = findViewById(R.id.categoryVanilla);
        iconFavorite = findViewById(R.id.iconFavorite);
        iconCart = findViewById(R.id.iconCart);
        iconShare = findViewById(R.id.iconShare);
        iconProfile = findViewById(R.id.iconProfile);
        searchBar = findViewById(R.id.search_bar);
        productGrid = findViewById(R.id.productGrid);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Product> filtered = allProducts.stream()
                        .filter(p -> p.getName().toLowerCase().contains(s.toString().toLowerCase()))
                        .collect(Collectors.toList());
                productAdapter.updateProducts(filtered);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupProductList() {
        allProducts.add(new Product("Chocolate Cake", R.drawable.choco_straw1, "Chocolate Cakes", 500,
                new String[]{"Chocolate", "Cacao", "Cream"}));
        allProducts.add(new Product("Milk girl", R.drawable.milk_girl, "Berry Cakes", 550,
                new String[]{"Strawberry", "Sugar", "Cottage cheese"}));
        allProducts.add(new Product("Cheesecake", R.drawable.cheesecake, "Other Cakes", 520,
                new String[]{"Creamcheese", "Chocolate", "Cream"}));
        allProducts.add(new Product("Nostalgy", R.drawable.nostalgy, "Chocolate Cakes", 480,
                new String[]{"Vanile", "Dough", "Butter"}));
        allProducts.add(new Product("Beze Cake", R.drawable.beze, "Berry Cakes", 530,
                new String[]{"Berries", "Dough", "Cream"}));
        allProducts.add(new Product("Birthday Cake", R.drawable.birthday_cake, "Caramel Cakes", 510,
                new String[]{"Caramel", "Dough", "Cream"}));
        allProducts.add(new Product("Nostalgy", R.drawable.tart_nut, "Other Sweets", 480,
                new String[]{"Ваниль", "Dough", "Butter"}));
        allProducts.add(new Product("Beze Cake", R.drawable.tart_trad, "Other Sweets", 530,
                new String[]{"Berries", "Dough", "Cream"}));
        allProducts.add(new Product("Birthday Cake", R.drawable.tart_wheart, "Other Sweets", 510,
                new String[]{"Caramel", "Dough", "Cream"}));
        allProducts.add(new Product("Birthday Cake", R.drawable.pakhlava, "Other Sweets", 510,
                new String[]{"Caramel", "Dough", "Cream"}));
        allProducts.add(new Product("Beze Cake", R.drawable.nut_cookie, "Other Sweets", 530,
                new String[]{"Ягоды", "Dough", "Cream"}));
        allProducts.add(new Product("Birthday Cake", R.drawable.cookie, "Other Sweets", 510,
                new String[]{"Caramel", "Dough", "Cream"}));
        allProducts.add(new Product("Birthday Cake", R.drawable.cookie_heart, "Other Sweets", 510,
                new String[]{"Caramel", "Dough", "Cream"}));
        allProducts.add(new Product("Birthday Cake", R.drawable.eclair_nt, "Other Sweets", 510,
                new String[]{"Caramel", "Dough", "Cream"}));
    }

    private void setupClickListeners() {
        categoryChocolate.setOnClickListener(v -> filterProducts("Chocolate Cakes"));
        categoryHoney.setOnClickListener(v -> filterProducts("Honey Cakes"));
        categoryBerry.setOnClickListener(v -> filterProducts("Berry Cakes"));
        categoryCaramel.setOnClickListener(v -> filterProducts("Bento Cakes"));
        categoryVanilla.setOnClickListener(v -> filterProducts("Other Sweets"));

        iconFavorite.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
            intent.putExtra("favorites", (Serializable) new ArrayList<>(favoriteCakes));
            startActivity(intent);
        });

        iconCart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            intent.putExtra("cart", (Serializable) new ArrayList<>(cartCakes));
            startActivity(intent);
        });

        iconShare.setOnClickListener(v -> shareApp());
        iconProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.iconGoToCustom).setOnClickListener(v -> startActivity(new Intent(this, CustomActivity.class)));
    }

    private void loadFavorites() {
        if (auth.getCurrentUser() != null) {
            db.collection("users")
                    .document(auth.getCurrentUser().getUid())
                    .collection("favorites")
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        favoriteCakes.clear();
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
                            favoriteCakes.add(product);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to load favorites: " + e.getMessage(), e);
                        ToastUtils.showCustomToast(this, "error uploading the favorites", Toast.LENGTH_SHORT);
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
                        cartCakes.clear();
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
                            cartCakes.add(product);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to load cart: " + e.getMessage(), e);
                        ToastUtils.showCustomToast(this, "error uploading the cart", Toast.LENGTH_SHORT);
                    });
        }
    }

    private void filterProducts(String category) {
        List<Product> filtered = allProducts.stream()
                .filter(p -> {
                    switch (category) {
                        case "Chocolate Cakes": return p.getDescription().contains("Chocolate Cakes");
                        case "Honey Cakes": return p.getDescription().contains("Honey Cakes");
                        case "Berry Cakes": return p.getDescription().contains("Berry Cakes");
                        case "Bento Cakes": return p.getDescription().contains("Bento Cakes");
                        case "Other Sweets": return p.getDescription().contains("Other Sweets");
                        default: return true;
                    }
                })
                .collect(Collectors.toList());
        productAdapter.updateProducts(filtered);
        ToastUtils.showCustomToast(this, "selected category: " + category, Toast.LENGTH_SHORT);
    }

    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Try AvanyanCakes!");
        startActivity(Intent.createChooser(intent, "Share"));
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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}



















































