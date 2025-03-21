package com.example.afinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductPagerAdapter extends RecyclerView.Adapter<ProductPagerAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public ProductPagerAdapter(Context context, List<Product> productList, OnProductClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.cakeImage.setImageResource(product.getImageResId());
        holder.cakeName.setText(product.getName());
        holder.cakePrice.setText(String.format("%d ₽", product.getPrice()));
        holder.itemView.setOnClickListener(v -> listener.onProductClick(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView cakeImage;
        TextView cakeName, cakePrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            cakeImage = itemView.findViewById(R.id.productImage);
            cakeName = itemView.findViewById(R.id.productName);
            cakePrice = itemView.findViewById(R.id.productPrice);
        }
    }
}


