package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 cakePager = findViewById(R.id.cakePager);
        ViewPager2 cakePager2 = findViewById(R.id.cakePager2);

        int[] cakeImages = {
                R.drawable.banana4,
                R.drawable.straw1,
                R.drawable.filter4,
                R.drawable.banana4,
                R.drawable.straw1,
                R.drawable.filter4,
                R.drawable.banana4,
                R.drawable.straw1,
                R.drawable.filter4,
                R.drawable.banana4,
                R.drawable.straw1,
                R.drawable.filter4,
                R.drawable.banana4,
                R.drawable.straw1,
                R.drawable.filter4
        };

        String[] cakeNames = {
                "Banana cake",
                "Strawberry cake",
                "Chocolate cake"
        };

        String[] cakeDescriptions = {
                "Hamov",
                "Shat Hamov",
                "Ahavor Hamov"
        };

        String[][] ingredients = {
                {"Banana - 200 ₽", "Chocolate - 150 ₽", "Whipped Cream - 100 ₽"},
                {"Strawberry - 250 ₽", "Sugar - 50 ₽", "Cream Cheese - 200 ₽"},
                {"Chocolate - 300 ₽", "Cocoa - 150 ₽", "Whipped Cream - 100 ₽"}
        };


        Adapter adapter = new Adapter(this, cakeImages, imageResId -> {
            int position = -1;
            for (int i = 0; i < cakeImages.length; i++) {
                if (cakeImages[i] == imageResId) {
                    position = i % cakeNames.length;
                    break;
                }
            }
            if (position != -1) {
                Intent intent = new Intent(MainActivity.this, CakeDetalsActivity.class);
                intent.putExtra("imageResId", imageResId);
                intent.putExtra("cakeName", cakeNames[position]);
                intent.putExtra("cakeDescription", cakeDescriptions[position]);
                intent.putExtra("ingredients", ingredients[position]);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        cakePager.setAdapter(adapter);
        cakePager2.setAdapter(adapter);

        ImageView cake = findViewById(R.id.cake);
        ImageView car = findViewById(R.id.car);
        ImageView message = findViewById(R.id.message);
        ImageView custom = findViewById(R.id.piece);
        ImageView filter = findViewById(R.id.filter);

        cake.setOnClickListener(v -> {
            cake.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).withEndAction(() -> {
                cake.animate().scaleX(1f).scaleY(1f).setDuration(200);
            });
        });
        car.setOnClickListener(v -> {
            car.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).withEndAction(() -> {
                car.animate().scaleX(1f).scaleY(1f).setDuration(200);
            });
        });
        message.setOnClickListener(v -> {
            message.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).withEndAction(() -> {
                message.animate().scaleX(1f).scaleY(1f).setDuration(200);
            });
        });
        custom.setOnClickListener(v -> {
            custom.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).withEndAction(new Runnable() {
                @Override
                public void run() {
                    custom.animate().scaleX(1f).scaleY(1f).setDuration(200);
                    Intent intent = new Intent(MainActivity.this, CustomActivity.class);
                    startActivity(intent);

                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });
        });
        filter.setOnClickListener(v -> openFilterDialog());
    }

    public void openFilterDialog() {
        FilterDialogFragment filterDialog = new FilterDialogFragment();
        filterDialog.show(getSupportFragmentManager(), "filterDialog");
    }
}


