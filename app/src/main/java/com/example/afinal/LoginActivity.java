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
            Button testUserButton = findViewById(R.id.test_user_button);

            if (testUserButton == null) {
                Log.e(TAG, "View initialization failed: testUserButton is null");
                ToastUtils.showCustomToast(this, "interface error", Toast.LENGTH_SHORT);
                return;
            }



            if (emailField == null || passwordField == null || loginButton == null || signupLink == null) {
                Log.e(TAG, "View initialization failed: emailField=" + emailField + ", passwordField=" + passwordField + ", loginButton=" + loginButton + ", signupLink=" + signupLink);
                ToastUtils.showCustomToast(this, "interface error", Toast.LENGTH_SHORT);
                return;
            }

            loginButton.setOnClickListener(v -> {
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    ToastUtils.showCustomToast(this, "enter email and password", Toast.LENGTH_SHORT);
                    return;
                }

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Login successful for email: " + email);
                                ToastUtils.showCustomToast(this, "login sucsessful", Toast.LENGTH_SHORT);
                                startActivity(new Intent(this, MainActivity.class));
                                finish();
                            } else {
                                Log.e(TAG, "Login failed: " + task.getException().getMessage(), task.getException());
                                ToastUtils.showCustomToast(this, "login error " + task.getException().getMessage(), Toast.LENGTH_LONG);
                            }
                        });
            });

            testUserButton.setOnClickListener(v -> {
                String testEmail = "individualproject2025@gmail.com";
                String testPassword = "Samsung2025";

                auth.signInWithEmailAndPassword(testEmail, testPassword)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Test login successful");
                                ToastUtils.showCustomToast(this, "Test login successful", Toast.LENGTH_SHORT);
                                startActivity(new Intent(this, MainActivity.class));
                                finish();
                            } else {
                                Log.e(TAG, "Test login failed: " + task.getException().getMessage(), task.getException());
                                ToastUtils.showCustomToast(this, "login error " + task.getException().getMessage(), Toast.LENGTH_LONG);
                            }
                        });
            });


            signupLink.setOnClickListener(v -> {
                startActivity(new Intent(this, SignupActivity.class));
                finish();
            });
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage(), e);
            ToastUtils.showCustomToast(this, "error " + e.getMessage(), Toast.LENGTH_SHORT);
        }
    }
}