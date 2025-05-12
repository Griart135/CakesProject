package com.example.afinal;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrdersHistoryActivity extends AppCompatActivity {
    private static final String TAG = "OrdersHistoryActivity";
    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private TextView emptyMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_history);

        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            recyclerView = findViewById(R.id.orders_recycler_view);
            emptyMessage = findViewById(R.id.empty_message);

            if (recyclerView == null || emptyMessage == null) {
                Log.e(TAG, "View initialization failed");
                ToastUtils.showCustomToast(this, "Interface error", Toast.LENGTH_SHORT);
                return;
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new OrderAdapter(new ArrayList<>());
            recyclerView.setAdapter(adapter);

            db = FirebaseFirestore.getInstance();
            auth = FirebaseAuth.getInstance();

            loadOrders();
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage(), e);
            ToastUtils.showCustomToast(this, "error " + e.getMessage(), Toast.LENGTH_SHORT);
        }
    }

    private void loadOrders() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            ToastUtils.showCustomToast(this, "Please signin", Toast.LENGTH_SHORT);
            finish();
            return;
        }

        db.collection("users")
                .document(user.getUid())
                .collection("orders")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Map<String, Object>> orders = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        orders.add(doc.getData());
                    }
                    adapter.setOrders(orders);
                    emptyMessage.setVisibility(orders.isEmpty() ? View.VISIBLE : View.GONE);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading orders: " + e.getMessage(), e);
                    ToastUtils.showCustomToast(this, "Error loading orders: " + e.getMessage(), Toast.LENGTH_LONG);
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}