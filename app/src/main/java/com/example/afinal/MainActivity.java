package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    private TextView categoryChocolate, categoryHoney, categoryBerry, categoryCaramel, categoryVanilla;
    private ImageView iconFavorite, iconCart, iconShare, iconProfile;
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


        db.collection("cakes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            String name = document.getString("name");
                            Long price = document.getLong("price");
                            Log.d("Firestore", "Document: " + name + ", Price: " + price);
                        }
                    }
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

        productGrid = findViewById(R.id.productGrid);
    }

    private void setupProductList() {
        allProducts.add(new Product("Chocolate Cake", R.drawable.choco_straw1, "Шоколадный торт", 500,
                new String[]{"Шоколад", "Какао", "Сливки"}));
        allProducts.add(new Product("Strawberry Cake", R.drawable.straw1, "Клубничный торт", 550,
                new String[]{"Клубника", "Сахар", "Творожный сыр"}));
        allProducts.add(new Product("Banana Cake", R.drawable.cheesecake, "Банановый торт", 520,
                new String[]{"Банан", "Шоколад", "Крем"}));
        allProducts.add(new Product("Vanilla Cake", R.drawable.nostalgy, "Ванильный торт", 480,
                new String[]{"Ваниль", "Мука", "Масло"}));
        allProducts.add(new Product("Berry Cake", R.drawable.choco_straw1, "Ягодный торт", 530,
                new String[]{"Ягоды", "Мука", "Крем"}));
        allProducts.add(new Product("Caramel Cake", R.drawable.nostalgy, "Карамельный торт", 510,
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

//        iconCart.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, CartActivity.class);
//            intent.putParcelableArrayListExtra("cart", new ArrayList<>(cartCakes));
//            startActivity(intent);
//        });

        iconShare.setOnClickListener(v -> shareApp());
        iconProfile.setOnClickListener(v -> goToProfile());

        findViewById(R.id.iconGoToCustom).setOnClickListener(v -> openCustomActivity());
    }

    private void filterProducts(String category) {
        List<Product> filtered = new ArrayList<>();
        switch (category) {
            case "Chocolate Cakes":
                filtered = allProducts.stream()
                        .filter(p -> p.getDescription().contains("Шоколадный"))
                        .collect(Collectors.toList());
                break;
            case "Honey Cakes":
                filtered = allProducts.stream()
                        .filter(p -> p.getDescription().contains("Медовый"))
                        .collect(Collectors.toList());
                break;
            case "Berry Cakes":
                filtered = allProducts.stream()
                        .filter(p -> p.getDescription().contains("Ягодный"))
                        .collect(Collectors.toList());
                break;
            case "Bento Cakes":
                filtered = allProducts.stream()
                        .filter(p -> p.getDescription().contains("Карамельный"))
                        .collect(Collectors.toList());
                break;
            case "Other Sweets":
                filtered = allProducts.stream()
                        .filter(p -> p.getDescription().contains("Ванильный"))
                        .collect(Collectors.toList());
                break;
        }
        productAdapter.updateProducts(filtered);
        Toast.makeText(this, "Выбрана категория: " + category, Toast.LENGTH_SHORT).show();
    }

    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Попробуй AvanyanCakes! Лучшие торты здесь!");
        startActivity(Intent.createChooser(intent, "Поделиться"));
    }

    private void goToProfile() {
        Toast.makeText(this, "Переход в профиль (заглушка)", Toast.LENGTH_SHORT).show();
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
        startActivity(new Intent(this, CustomActivity.class));
    }
}









