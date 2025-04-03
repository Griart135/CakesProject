package com.example.afinal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CakeAdapter extends RecyclerView.Adapter<CakeAdapter.CakeViewHolder> {
    private List<Cake> cakes;

    public CakeAdapter(List<Cake> cakes) {
        this.cakes = cakes;
    }

    @NonNull
    @Override
    public CakeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cake_item, parent, false);
        return new CakeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CakeViewHolder holder, int position) {
        Cake cake = cakes.get(position);
        if (cake != null) {
            holder.cakeName.setText(cake.getName());
            holder.cakeImage.setImageResource(cake.getImageResId());
        }
    }

    @Override
    public int getItemCount() {
        return cakes != null ? cakes.size() : 0;
    }

    public void updateCakes(List<Cake> cakes) {
        if (cakes != null) {
            this.cakes = cakes;
            notifyDataSetChanged();
        }
    }

    public void addCake(Cake cake) {
        if (cake != null) {
            cakes.add(cake);
            notifyItemInserted(cakes.size() - 1);
        }
    }

    public void removeCake(int position) {
        if (position >= 0 && position < cakes.size()) {
            cakes.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clearCakes() {
        if (cakes != null && !cakes.isEmpty()) {
            cakes.clear();
            notifyDataSetChanged();
        }
    }

    public static class CakeViewHolder extends RecyclerView.ViewHolder {
        TextView cakeName;
        ImageView cakeImage;

        public CakeViewHolder(View itemView) {
            super(itemView);
            cakeName = itemView.findViewById(R.id.cake_name);
            cakeImage = itemView.findViewById(R.id.cake_image);
        }
    }
}



