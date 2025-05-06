package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private TextView profileName, profileEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        auth = FirebaseAuth.getInstance();
        profileEmail = findViewById(R.id.profile_email);
        profileName = findViewById(R.id.profile_name);
        View ordersButton = findViewById(R.id.orders_button);
        View editProfileButton = findViewById(R.id.edit_profile_button);
        View historyOrders = findViewById(R.id.history_button);
        View logoutButton = findViewById(R.id.logout_button);

        historyOrders.setOnClickListener(v -> {
            startActivity(new Intent(this, OrdersHistoryActivity.class));
            finish();
        });
        editProfileButton.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
            finish();
        });
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            profileEmail.setText(user.getEmail());
        }
        logoutButton.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}