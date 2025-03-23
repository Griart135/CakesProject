package com.example.afinal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class OrderActivity extends AppCompatActivity {

    private TextView heightLabel, radiusLabel;
    private SeekBar heightSeekBar, radiusSeekBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        heightLabel = findViewById(R.id.order_height_label);
        radiusLabel = findViewById(R.id.order_radius_label);
        heightSeekBar = findViewById(R.id.order_height_seekbar);
        radiusSeekBar = findViewById(R.id.order_radius_seekbar);
        ImageView orderCakeImage = findViewById(R.id.cake_image);
        TextView cakeNameTextView = findViewById(R.id.cake_name);


        int imageResId = getIntent().getIntExtra("ImageResId", -1);
        String cakeName = getIntent().getStringExtra("cake_name");

        if (cakeName != null) {
            cakeNameTextView.setText(cakeName);
        }


        if (imageResId != -1) {
            orderCakeImage.setImageResource(imageResId);
        } else {
            orderCakeImage.setVisibility(View.GONE);
        }

        heightSeekBar.setProgress(10);
        radiusSeekBar.setProgress(50);

        heightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double height = Math.round(progress / 10.0 * 10.0) / 10.0;
                heightLabel.setText("Height: " + height + " cm");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double radius = Math.round(progress / 10.0 * 10.0) / 10.0;
                radiusLabel.setText("Radius: " + radius + " cm");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
}



