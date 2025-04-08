package com.example.afinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> products;
    private OnProductClickListener onProductClickListener;
    private OnProductActionListener actionListener;

    public ProductAdapter(Context context, List<Product> products,
                          OnProductClickListener onProductClickListener,
                          OnProductActionListener actionListener) {
        this.context = context;
        this.products = products;
        this.onProductClickListener = onProductClickListener;
        this.actionListener = actionListener;
    }

    public ProductAdapter(Context context, List<Product> products,
                          OnProductClickListener onProductClickListener) {
        this(context, products, onProductClickListener, new OnProductActionListener() {
            @Override public void onAddToFavorites(Product product) {}
            @Override public void onAddToCart(Product product) {}
        });
    }

    public void updateProducts(List<Product> newProducts) {
        this.products = newProducts;
        notifyDataSetChanged();
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.productName.setText(product.getName());
        holder.productDescription.setText(product.getDescription());
        holder.productPrice.setText(String.format("%d ₽", product.getPrice()));
        holder.productImage.setImageResource(product.getImageResId());

        holder.itemView.setOnClickListener(v -> onProductClickListener.onProductClick(product));

        holder.btnFavorite.setOnClickListener(v -> actionListener.onAddToFavorites(product));
        holder.btnCart.setOnClickListener(v -> actionListener.onAddToCart(product));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public interface OnProductActionListener {
        void onAddToFavorites(Product product);
        void onAddToCart(Product product);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productDescription, productPrice;
        ImageView productImage;
        ImageButton btnFavorite, btnCart;

        public ProductViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            btnFavorite = itemView.findViewById(R.id.favoriteIcon);
            btnCart = itemView.findViewById(R.id.cartIcon);
        }
    }
}

