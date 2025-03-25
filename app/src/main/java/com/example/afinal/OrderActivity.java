package com.example.afinal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OrderActivity extends AppCompatActivity {

    private TextView heightLabel, radiusLabel, slicesLabel;
    private SeekBar heightSeekBar, radiusSeekBar;
    private NumberPicker slicesPicker;
    private Button orderSlicesButton;

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
        slicesPicker = findViewById(R.id.order_slices_picker);
        orderSlicesButton = findViewById(R.id.order_slices_button);
        slicesLabel = findViewById(R.id.slices_label);



        slicesPicker.setMinValue(1);
        slicesPicker.setMaxValue(12);
        slicesPicker.setWrapSelectorWheel(true);

        slicesPicker.setOnValueChangedListener((picker, oldVal, newVal) ->
                slicesLabel.setText("Куски: " + newVal)
        );



        orderSlicesButton.setOnClickListener(v -> {
            int selectedSlices = slicesPicker.getValue();
            Toast.makeText(OrderActivity.this, "Вы выбрали " + selectedSlices + " кусочков", Toast.LENGTH_SHORT).show();
        });

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
        radiusSeekBar.setProgress(20);

        heightSeekBar.setMax(45);
        radiusSeekBar.setMax(25);

        heightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                heightLabel.setText("Height: " + (progress + 5) + " cm");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radiusLabel.setText("Radius: " + (progress + 5) + " cm");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        Button orderButton = findViewById(R.id.order_button);
        orderButton.setOnClickListener(v -> {
            int height = heightSeekBar.getProgress() + 5;
            int radius = radiusSeekBar.getProgress() + 5;

            Toast.makeText(OrderActivity.this, "Вы заказали торт с высотой " + height + " см и радиусом " + radius + " см", Toast.LENGTH_SHORT).show();
        });
    }
}



