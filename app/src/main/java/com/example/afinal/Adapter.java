package com.example.afinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context context;
    private int[] images;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int imageResId);
    }

    public Adapter(Context context, int[] images, OnItemClickListener listener) {
        this.context = context;
        this.images = images;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cake, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < 0 || position >= images.length){
            return;
        }
        holder.cakeImageView.setImageResource(images[position]);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(images[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cakeImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cakeImageView = itemView.findViewById(R.id.cakeImageView);
        }
    }
}

