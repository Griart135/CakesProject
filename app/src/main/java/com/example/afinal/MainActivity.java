package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    db = FirebaseFirestore.getInstance();

        Map<String, Object> cake = new HashMap<>();
        cake.put("name", "Chocolate Cake");
        cake.put("price", 20);

        db.collection("cakes")
                .add(cake)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firestore", "DocumentSnapshot added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error adding document", e);
                });

        db.collection("cakes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            String name = document.getString("name");
                            Long price = document.getLong("price");

                            Log.d("Firestore", "Document ID: " + document.getId() + ", Name: " + name + ", Price: " + price);
                        }
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                });

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
        products.add(new Product("Banana Cake", R.drawable.cheesecake, "Банановый торт", 520,
                new String[] {"Банан", "Шоколад ", "Крем"}
        ));
        products.add(new Product("Vanilla Cake", R.drawable.nostalgy, "Ванильный торт", 480,
                new String[] {"Ваниль", "Мука ", "Масло"}
        ));
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








