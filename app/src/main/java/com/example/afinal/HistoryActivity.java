package com.example.afinal;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private ProgressBar progressBar;
    private List<Order> orderList = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private static final String ADMIN_EMAIL = "avanyancakes@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.history_recycler_view);
        progressBar = findViewById(R.id.history_progress_bar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new HistoryAdapter(orderList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        loadOrders();
    }

    private void loadOrders() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Log.e("HistoryActivity", "User is not authenticated");
            ToastUtils.showCustomToast(this, "You didn't login", Toast.LENGTH_SHORT);
            return;
        }
        Log.d("HistoryActivity", "Authenticated user: " + user.getUid());

        progressBar.setVisibility(View.VISIBLE);

        Query query;
        if (Objects.equals(user.getEmail(), ADMIN_EMAIL)) {
            query = db.collection("orders");
        } else {
            query = db.collection("orders").whereEqualTo("userId", user.getUid());
        }

        query.get().addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                orderList.clear();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    Order order = doc.toObject(Order.class);
                    orderList.add(order);
                }
                adapter.notifyDataSetChanged();
            } else {
                ToastUtils.showCustomToast(this, "Error loading orders", Toast.LENGTH_SHORT);
                Log.e("HistoryActivity", "Error loading orders", task.getException());
            }
        });
    }
}
