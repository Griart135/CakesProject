package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 cakePager = findViewById(R.id.cakePager);

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

        Adapter Adapter = new Adapter(this, cakeImages);
        cakePager.setAdapter(Adapter);

        ImageView cake = findViewById(R.id.cake);
        ImageView car = findViewById(R.id.car);
        ImageView message = findViewById(R.id.message);
        ImageView custom = findViewById(R.id.piece);

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
    }
}

