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
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
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
                Toast.makeText(this, "Главная", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_custom) {
                startActivity(new Intent(this, CustomActivity.class));
            } else if (id == R.id.nav_profile) {
                Toast.makeText(this, "Профиль", Toast.LENGTH_SHORT).show();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        initViews();
        setupProductList();

        productAdapter = new ProductAdapter(this, allProducts, this::openProductDetails, new ProductAdapter.OnProductActionListener() {
            @Override
            public void onAddToFavorites(Product product) {
                if (!favoriteCakes.contains(product)) {
                    favoriteCakes.add(product);
                    Toast.makeText(MainActivity.this, "Добавлено в избранное", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAddToCart(Product product) {
                if (!cartCakes.contains(product)) {
                    cartCakes.add(product);
                    Toast.makeText(MainActivity.this, "Добавлено в корзину", Toast.LENGTH_SHORT).show();
                }
            }
        });
        productGrid.setLayoutManager(new LinearLayoutManager(this));
        productGrid.setAdapter(productAdapter);

        setupClickListeners();

        db.collection("cakes").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                task.getResult().forEach(document -> {
                    String name = document.getString("name");
                    Long price = document.getLong("price");
                    Log.d("Firestore", "Document: " + name + ", Price: " + price);
                });
            } else {
                Log.e("Firestore", "Error getting cakes", task.getException());
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
        allProducts.add(new Product("Chocolate Cake", R.drawable.choco_straw1, "Шоколадный торт", 500,
                new String[]{"Шоколад", "Какао", "Сливки"}));
        allProducts.add(new Product("Milk girl", R.drawable.milk_girl, "Клубничный торт", 550,
                new String[]{"Клубника", "Сахар", "Творожный сыр"}));
        allProducts.add(new Product("Cheesecake", R.drawable.cheesecake, "Банановый торт", 520,
                new String[]{"Банан", "Шоколад", "Крем"}));
        allProducts.add(new Product("Nostalgy", R.drawable.nostalgy, "Ванильный торт", 480,
                new String[]{"Ваниль", "Мука", "Масло"}));
        allProducts.add(new Product("Beze Cake", R.drawable.beze, "Ягодный торт", 530,
                new String[]{"Ягоды", "Мука", "Крем"}));
        allProducts.add(new Product("Birthday Cake", R.drawable.birthday_cake, "Карамельный торт", 510,
                new String[]{"Карамель", "Мука", "Крем"}));
    }

    private void setupClickListeners() {
        categoryChocolate.setOnClickListener(v -> filterProducts("Chocolate Cakes"));
        categoryHoney.setOnClickListener(v -> filterProducts("Honey Cakes"));
        categoryBerry.setOnClickListener(v -> filterProducts("Berry Cakes"));
        categoryCaramel.setOnClickListener(v -> filterProducts("Bento Cakes"));
        categoryVanilla.setOnClickListener(v -> filterProducts("Other Sweets"));

        iconFavorite.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
            intent.putParcelableArrayListExtra("favorites", new ArrayList<>(favoriteCakes));
            startActivity(intent);
        });

        iconCart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            intent.putParcelableArrayListExtra("cart", new ArrayList<>(cartCakes));
            startActivity(intent);
        });

        iconShare.setOnClickListener(v -> shareApp());
        iconProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.iconGoToCustom).setOnClickListener(v -> startActivity(new Intent(this, CustomActivity.class)));
    }

    private void filterProducts(String category) {
        List<Product> filtered = allProducts.stream()
                .filter(p -> {
                    switch (category) {
                        case "Chocolate Cakes": return p.getDescription().contains("Шоколадный");
                        case "Honey Cakes": return p.getDescription().contains("Медовый");
                        case "Berry Cakes": return p.getDescription().contains("Ягодный");
                        case "Bento Cakes": return p.getDescription().contains("Карамельный");
                        case "Other Sweets": return p.getDescription().contains("Ванильный");
                        default: return true;
                    }
                })
                .collect(Collectors.toList());
        productAdapter.updateProducts(filtered);
        Toast.makeText(this, "Выбрана категория: " + category, Toast.LENGTH_SHORT).show();
    }

    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Попробуй AvanyanCakes! Лучшие торты здесь!");
        startActivity(Intent.createChooser(intent, "Поделиться"));
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


















