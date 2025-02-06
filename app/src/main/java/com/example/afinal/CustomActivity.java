package com.example.afinal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class CustomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piece);

        ImageView banana = findViewById(R.id.bananaImage);
        ImageView plate = findViewById(R.id.plate);
        Button addBananaButton = findViewById(R.id.addBananaButton);

        ConstraintLayout constraintLayout = findViewById(R.id.layoutPiece);

        addBananaButton.setOnClickListener(v -> {
            banana.animate()
                    .x(plate.getX() +50)
                    .y(plate.getY() + 200)
                    .setDuration(500)
                    .start();

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);

            constraintSet.connect(banana.getId(), ConstraintSet.TOP, R.id.plate, ConstraintSet.TOP, +50);
            constraintSet.connect(banana.getId(), ConstraintSet.START, R.id.plate, ConstraintSet.START, +200);

            constraintSet.applyTo(constraintLayout);
        });
    }
}

