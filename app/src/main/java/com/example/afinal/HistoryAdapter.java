package com.example.afinal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private final List<Order> orderList;

    public HistoryAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.name.setText("cake: " + order.getName());
        holder.details.setText("height: " + order.getHeight() + " cm, Radius: " + order.getRadius() + " cm, slices: " + order.getSlices());
        holder.address.setText("Adress: " + order.getAddress());
        holder.price.setText("Price: " + order.getPrice() + "₽");
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView name, details, address, price;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.order_name);
            details = itemView.findViewById(R.id.order_details);
            address = itemView.findViewById(R.id.order_address);
            price = itemView.findViewById(R.id.order_price);
        }
    }
}

