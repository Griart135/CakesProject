package com.example.afinal;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private final List<Ingredient> ingredients;
    private final OnIngredientClickListener listener;

    public interface OnIngredientClickListener {
        void onIngredientClick(Ingredient ingredient);
    }

    public IngredientAdapter(List<Ingredient> ingredients, OnIngredientClickListener listener) {
        this.ingredients = ingredients;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.nameTextView.setText(ingredient.getName());
        holder.priceTextView.setText(ingredient.getPrice() + " ₽");

        holder.addButton.setOnClickListener(v -> listener.onIngredientClick(ingredient));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, priceTextView;
        Button addButton;


        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.ingredient_name);
            priceTextView = itemView.findViewById(R.id.ingredient_price);
            addButton = itemView.findViewById(R.id.btn_add);
        }
    }
}

