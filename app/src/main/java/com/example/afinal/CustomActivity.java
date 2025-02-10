package com.example.afinal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class CustomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piece);

        Button addBananaButton = findViewById(R.id.addBananaButton);
        ConstraintLayout constraintLayout = findViewById(R.id.layoutPiece);

        addBananaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BananaPlus(constraintLayout);
            }
        });
    }

    private void BananaPlus(ConstraintLayout layout) {
        TextView bananPlus = new TextView(this);
        bananPlus.setId(View.generateViewId());
        bananPlus.setText("Банан");
        bananPlus.setTextSize(18);
        bananPlus.setTextColor(getResources().getColor(android.R.color.black));

        layout.addView(bananPlus);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);

        constraintSet.connect(bananPlus.getId(), ConstraintSet.TOP, R.id.plateBackground, ConstraintSet.TOP, 16);
        constraintSet.connect(bananPlus.getId(), ConstraintSet.START, R.id.plateBackground, ConstraintSet.START, 16);

        constraintSet.applyTo(layout);
    }
}




