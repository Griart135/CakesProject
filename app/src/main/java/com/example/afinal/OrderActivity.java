package com.example.afinal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {

    private TextView heightLabel, radiusLabel, slicesLabel;
    private SeekBar heightSeekBar, radiusSeekBar;
    private NumberPicker slicesPicker;
    private Button orderSlicesButton, clearSelectionButton, confirmButton;
    private FirebaseAuth auth;
    private EditText addressInput;
    private Product selectedProduct;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        auth = FirebaseAuth.getInstance();

        heightLabel = findViewById(R.id.order_height_label);
        radiusLabel = findViewById(R.id.order_radius_label);
        heightSeekBar = findViewById(R.id.order_height_seekbar);
        radiusSeekBar = findViewById(R.id.order_radius_seekbar);
        ImageView orderCakeImage = findViewById(R.id.cake_image);
        TextView cakeNameTextView = findViewById(R.id.cake_name);
        slicesPicker = findViewById(R.id.order_slices_picker);
        orderSlicesButton = findViewById(R.id.order_slices_button);
        slicesLabel = findViewById(R.id.slices_label);
        clearSelectionButton = findViewById(R.id.clear_button);
        addressInput = findViewById(R.id.address_input);

        clearSelectionButton.setOnClickListener(v -> {
            heightSeekBar.setProgress(10);
            radiusSeekBar.setProgress(20);
            slicesPicker.setValue(1);
            heightLabel.setText("Height: 10 cm");
            radiusLabel.setText("Radius: 20 cm");
            slicesLabel.setText("Slices: 1");
        });

        slicesPicker.setMinValue(1);
        slicesPicker.setMaxValue(12);
        slicesPicker.setWrapSelectorWheel(true);

        slicesPicker.setOnValueChangedListener((picker, oldVal, newVal) ->
                slicesLabel.setText("Slices: " + newVal)
        );

        orderSlicesButton.setOnClickListener(v -> {
            int selectedSlices = slicesPicker.getValue();
            ToastUtils.showCustomToast(OrderActivity.this, "You selected " + selectedSlices + " slices", Toast.LENGTH_SHORT);
        });

//        int imageResId = getIntent().getIntExtra("ImageResId", -1);
//        String cakeName = getIntent().getStringExtra("cake_name");

//        if (cakeName != null) {
//            cakeNameTextView.setText(cakeName);
//        }
//
//        if (imageResId != -1) {
//            orderCakeImage.setImageResource(imageResId);
//        } else {
//            orderCakeImage.setVisibility(View.GONE);
//        }

        heightSeekBar.setProgress(10);
        radiusSeekBar.setProgress(20);

        heightSeekBar.setMax(45);
        radiusSeekBar.setMax(25);

        heightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                heightLabel.setText("Height: " + (progress + 5) + " cm");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radiusLabel.setText("Radius: " + (progress + 5) + " cm");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        String cakeName = getIntent().getStringExtra("cake_name");
        String cakeDescription = getIntent().getStringExtra("cake_description");
        int cakePrice = getIntent().getIntExtra("cake_price", 0);
        String[] cakeIngredients = getIntent().getStringArrayExtra("cake_ingredients");
        int imageResId = getIntent().getIntExtra("ImageResId", -1);

        selectedProduct = new Product(
                cakeName != null ? cakeName : "Unknown Cake",
                imageResId,
                cakeDescription != null ? cakeDescription : "",
                cakePrice,
                cakeIngredients != null ? cakeIngredients : new String[]{}
        );

        if (cakeName != null) {
            cakeNameTextView.setText(cakeName);
        }
        if (imageResId != -1) {
            orderCakeImage.setImageResource(imageResId);
        }

        Button orderButton = findViewById(R.id.confirm_order_button);
        orderButton.setOnClickListener(v -> {
            int height = heightSeekBar.getProgress() + 5;
            int radius = radiusSeekBar.getProgress() + 5;
            int slices = slicesPicker.getValue();
            String address = addressInput.getText().toString().trim();

            if (address.isEmpty()) {
                ToastUtils.showCustomToast(this, "Enter your adress", Toast.LENGTH_SHORT);
                return;
            }

            ConfirmOrderDialogFragment dialog = ConfirmOrderDialogFragment.newInstance(cakeName != null ? cakeName : "Unknown Cake", height, radius, slices, address);
            dialog.setOnConfirmListener(() -> {
                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser == null) {
                    auth.signInAnonymously()
                            .addOnCompleteListener(this, task -> {
                                if (task.isSuccessful()) {
                                    Log.d("Auth", "Anonym login sucsessful " + auth.getCurrentUser().getUid());
                                    saveOrder();
                                } else {
                                    Log.e("Auth", "anonym error", task.getException());
                                    ToastUtils.showCustomToast(OrderActivity.this, "error logging in", Toast.LENGTH_SHORT);
                                }
                            });
                } else {
                    saveOrder();
                }
            });
            dialog.show(getSupportFragmentManager(), "ConfirmOrderDialog");
        });
    }

    private void saveOrder() {
        int height = heightSeekBar.getProgress() + 5;
        int radius = radiusSeekBar.getProgress() + 5;
        int slices = slicesPicker.getValue();
        String address = addressInput.getText().toString().trim();

        if (address.isEmpty()) {
            Toast.makeText(this, "Enter your adress", Toast.LENGTH_SHORT).show();
            return;
        }

        Product cake = selectedProduct;

        saveOrderToFirestore(selectedProduct, height, radius, slices, address);

        ToastUtils.showCustomToast(OrderActivity.this, "You ordered a cake with height of " + height
                + " cm, radius of " + radius + " cm & " + slices + " slices.\\nDeliviery with this adress: " +
                address, Toast.LENGTH_SHORT);
    }

    public void saveOrderToFirestore(Product product, int height, int radius, int slices,  String address) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        Map<String, Object> orderData = new HashMap<>();
        orderData.put("name", product.getName());
        orderData.put("description", product.getDescription());
        orderData.put("price", product.getPrice());
        orderData.put("ingredients", Arrays.asList(product.getIngredients()));
        orderData.put("height", height);
        orderData.put("radius", radius);
        orderData.put("slices", slices);
        orderData.put("address", address);
        orderData.put("userId", currentUser.getUid());

        db.collection("orders")
                .add(orderData)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firestore", "Order saved with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error adding document", e);
                });
    }

    private Map<String, Object> createOrderData(Product product) {
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("name", product.getName());
        orderData.put("description", product.getDescription());
        orderData.put("price", product.getPrice());
        orderData.put("ingredients", Arrays.asList(product.getIngredients()));
        return orderData;
    }
}

















