package com.example.afinal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AdminLoginFragment extends DialogFragment {
    private static final String ADMIN_EMAIL = "avanyancakes@gmail.com";
    private static final String ADMIN_PASSWORD = "avanyancakesadmin2025";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_login, container, false);

        EditText emailField = view.findViewById(R.id.admin_email_field);
        EditText passwordField = view.findViewById(R.id.admin_password_field);
        Button loginButton = view.findViewById(R.id.admin_login_button);
        Button cancelButton = view.findViewById(R.id.admin_cancel_button);

        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                ToastUtils.showCustomToast(requireContext(), "enter email and password", android.widget.Toast.LENGTH_SHORT);
                return;
            }

            if (ADMIN_EMAIL.equals(email) && ADMIN_PASSWORD.equals(password)) {
                Context context = requireContext();
                Intent intent = new Intent(context, AdminPanelActivity.class);
                startActivity(intent);
                dismiss();
            } else {
                ToastUtils.showCustomToast(requireContext(), "wrong details", android.widget.Toast.LENGTH_SHORT);
            }
        });

        cancelButton.setOnClickListener(v -> dismiss());

        return view;
    }
}
