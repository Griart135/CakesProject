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

            if (loginButton != null) {
                loginButton.setOnClickListener(v -> {
                    String email = emailField != null ? emailField.getText().toString().trim() : "";
                    String password = passwordField != null ? passwordField.getText().toString().trim() : "";

                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(this, "enter email and password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(this, "login sucsessful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(this, MainActivity.class));
                                    finish();
                                } else {
                                    Log.e(TAG, "Login failed", task.getException());
                                    Toast.makeText(this, "Login error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                });
            }

            TextView signupLink = findViewById(R.id.signup_link);
            if (signupLink != null) {
                signupLink.setOnClickListener(v -> {
                    startActivity(new Intent(this, SignupActivity.class));
                    finish();
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate", e);
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}
