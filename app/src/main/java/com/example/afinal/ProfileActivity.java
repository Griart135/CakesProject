package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        auth = FirebaseAuth.getInstance();
        TextView emailView = findViewById(R.id.profile_email);
        Button logoutButton = findViewById(R.id.logout_button);
        Button editProfile = findViewById(R.id.edit_profile_button);
        Button historyOrders = findViewById(R.id.orders_button);

        historyOrders.setOnClickListener(v -> {
            startActivity(new Intent(this, OrdersHistoryActivity.class));
            finish();
        });
        editProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
            finish();
        });
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            emailView.setText(user.getEmail());
        }
        logoutButton.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}