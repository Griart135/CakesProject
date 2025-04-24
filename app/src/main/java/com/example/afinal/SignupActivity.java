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
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        try {
            auth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();

            EditText emailField = findViewById(R.id.email_field);
            EditText passwordField = findViewById(R.id.password_field);
            EditText nameField = findViewById(R.id.name_field);
            Button signupButton = findViewById(R.id.signup_button);

            if (signupButton != null) {
                signupButton.setOnClickListener(v -> {
                    String email = emailField != null ? emailField.getText().toString().trim() : "";
                    String password = passwordField != null ? passwordField.getText().toString().trim() : "";
                    String name = nameField != null ? nameField.getText().toString().trim() : "";

                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(this, "enter email and password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    String userId = auth.getCurrentUser().getUid();
                                    Map<String, Object> userProfile = new HashMap<>();
                                    userProfile.put("email", email);
                                    if (!name.isEmpty()) {
                                        userProfile.put("name", name);
                                    }

                                    db.collection("users").document(userId).set(userProfile)
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(this, "signup sucsessful", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(this, MainActivity.class));
                                                finish();
                                            })
                                            .addOnFailureListener(e -> {
                                                Log.e(TAG, "Failed to save user profile", e);
                                                Toast.makeText(this, "error saving account", Toast.LENGTH_SHORT).show();
                                            });
                                } else {
                                    Log.e(TAG, "Signup failed", task.getException());
                                    Toast.makeText(this, "signup error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                });
            }

            TextView loginLink = findViewById(R.id.login_link);
            if (loginLink != null) {
                loginLink.setOnClickListener(v -> {
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate", e);
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }
    }
}