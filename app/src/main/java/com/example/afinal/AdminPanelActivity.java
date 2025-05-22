package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminPanelActivity extends AppCompatActivity {

    private Button viewOrdersButton;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        viewOrdersButton = findViewById(R.id.view_orders_button);
        logoutButton = findViewById(R.id.logout_button);

        viewOrdersButton.setOnClickListener(v -> {
            ToastUtils.showCustomToast(this, "orders", Toast.LENGTH_SHORT);
            Intent intent = new Intent(AdminPanelActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {
            ToastUtils.showCustomToast(this, "exit from admin panel", Toast.LENGTH_SHORT);
            finish();
        });
    }
}
