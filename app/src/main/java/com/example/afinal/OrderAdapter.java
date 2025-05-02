package com.example.afinal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Map;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Map<String, Object>> orders;

    public OrderAdapter(List<Map<String, Object>> orders) {
        this.orders = orders;
    }

    public void setOrders(List<Map<String, Object>> newOrders) {
        this.orders = newOrders;
        notifyDataSetChanged();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, detailsText;

        public OrderViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.order_name);
            detailsText = itemView.findViewById(R.id.order_details);
        }
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Map<String, Object> order = orders.get(position);
        String name = order.get("name") != null ? order.get("name").toString() : "Unknown Cake";
        String height = order.get("height") != null ? order.get("height").toString() : "N/A";
        String radius = order.get("radius") != null ? order.get("radius").toString() : "N/A";
        String slices = order.get("slices") != null ? order.get("slices").toString() : "N/A";
        String address = order.get("address") != null ? order.get("address").toString() : "N/A";

        holder.nameText.setText(name);
        String details = String.format("Height: %s см\nRadius: %s см\nSlices: %s\nAddress: %s",
                height, radius, slices, address);
        holder.detailsText.setText(details);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}