package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            auth = FirebaseAuth.getInstance();

            EditText emailField = findViewById(R.id.email_field);
            EditText passwordField = findViewById(R.id.password_field);
            Button loginButton = findViewById(R.id.login_button);
            TextView signupLink = findViewById(R.id.signup_link);

            if (emailField == null || passwordField == null || loginButton == null || signupLink == null) {
                Log.e(TAG, "View initialization failed: emailField=" + emailField + ", passwordField=" + passwordField + ", loginButton=" + loginButton + ", signupLink=" + signupLink);
                Toast.makeText(this, "interface error", Toast.LENGTH_SHORT).show();
                return;
            }

            loginButton.setOnClickListener(v -> {
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Login successful for email: " + email);
                                Toast.makeText(this, "login sucsessful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, MainActivity.class));
                                finish();
                            } else {
                                Log.e(TAG, "Login failed: " + task.getException().getMessage(), task.getException());
                                Toast.makeText(this, "login error " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            });

            signupLink.setOnClickListener(v -> {
                startActivity(new Intent(this, SignupActivity.class));
                finish();
            });
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "error " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}