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
import com.google.firebase.auth.FirebaseUser;
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
            TextView loginLink = findViewById(R.id.login_link);

            if (emailField == null || passwordField == null || signupButton == null || loginLink == null) {
                Log.e(TAG, "View initialization failed: emailField=" + emailField + ", passwordField=" + passwordField + ", signupButton=" + signupButton + ", loginLink=" + loginLink);
                Toast.makeText(this, "interface error", Toast.LENGTH_SHORT).show();
                return;
            }

            signupButton.setOnClickListener(v -> {
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();
                String name = nameField != null ? nameField.getText().toString().trim() : "";

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "please enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(this, "password has to be more than 5 symbols", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = auth.getCurrentUser();
                                if (user == null) {
                                    Log.e(TAG, "FirebaseUser is null after successful signup");
                                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                String userId = user.getUid();
                                Map<String, Object> userProfile = new HashMap<>();
                                userProfile.put("email", email);
                                if (!name.isEmpty()) {
                                    userProfile.put("name", name);
                                }

                                Log.d(TAG, "Attempting to save user profile for UID: " + userId + ", Data: " + userProfile);
                                db.collection("users").document(userId).set(userProfile)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d(TAG, "User profile saved successfully for UID: " + userId);
                                            Toast.makeText(this, "registration sucsessful", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(this, MainActivity.class));
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e(TAG, "Failed to save user profile: " + e.getMessage(), e);
                                            Toast.makeText(this, "error saving account " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        });
                            } else {
                                Log.e(TAG, "Signup failed: " + task.getException().getMessage(), task.getException());
                                Toast.makeText(this, "error signup " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            });

            loginLink.setOnClickListener(v -> {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            });
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "error " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}