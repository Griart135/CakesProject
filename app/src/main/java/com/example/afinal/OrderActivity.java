package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OrderActivity extends AppCompatActivity {
    private int selectedSize = 1;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        ImageView cakeImage = findViewById(R.id.order_cake_image);
        TextView cakeName = findViewById(R.id.order_cake_name);
        TextView cakePrice = findViewById(R.id.order_cake_price);
        TextView sizeLabel = findViewById(R.id.order_size_label);
        SeekBar sizeSeekBar = findViewById(R.id.order_size_seekbar);
        Button confirmButton = findViewById(R.id.confirm_order_button);

        Intent intent = getIntent();
        int imageResId = intent.getIntExtra("imageResId", -1);
        String name = intent.getStringExtra("cakeName");
        String price = intent.getStringExtra("cakePrice");

        cakeImage.setImageResource(imageResId);
        cakeName.setText(name);
        cakePrice.setText(price);

        sizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedSize = progress + 1;
                sizeLabel.setText("Size : " + selectedSize + " kg");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        confirmButton.setOnClickListener(v -> {
            Toast.makeText(OrderActivity.this,
                    "Order Confirmed: " + name + " (" + selectedSize + " kg)", Toast.LENGTH_SHORT);
            finish();
        });
    }
}
