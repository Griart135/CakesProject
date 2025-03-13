package com.example.afinal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private final List<Ingredient> ingredients;
    private final OnIngredientChangeListener listener;
    private final Set<Ingredient> selectedIngredients = new HashSet<>();

    public interface OnIngredientChangeListener {
        void onIngredientChange(Ingredient ingredient, boolean isAdding);
    }

    public IngredientAdapter(List<Ingredient> ingredients, OnIngredientChangeListener listener) {
        this.ingredients = ingredients;
        this.listener = listener;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        boolean isSelected = selectedIngredients.contains(ingredient);

        holder.ingredientName.setText(ingredient.getName());
        holder.ingredientPrice.setText(ingredient.getPrice() + " ₽");
        holder.actionButton.setText(isSelected ? "Убрать" : "Добавить");

        holder.actionButton.setOnClickListener(v -> {
            if (isSelected) {
                selectedIngredients.remove(ingredient);
                listener.onIngredientChange(ingredient, false);
            } else {
                selectedIngredients.add(ingredient);
                listener.onIngredientChange(ingredient, true);
            }
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientName, ingredientPrice;
        Button actionButton;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredient_name);
            ingredientPrice = itemView.findViewById(R.id.ingredient_price);
            actionButton = itemView.findViewById(R.id.action_button);
        }
    }
}

