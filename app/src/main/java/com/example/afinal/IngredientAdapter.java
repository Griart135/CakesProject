package com.example.afinal;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        if (ingredient != null) {
            holder.ingredientName.setText(ingredient.getName());
            holder.ingredientPrice.setText(ingredient.getPrice() + " ₽");
            holder.actionButton.setText(isSelected ? "remove" : "add");

            holder.actionButton.setOnClickListener(v -> {
                Log.d("IngredientAdapter", "Clicked: " + ingredient.getName() + ", isAdding: " + !isSelected);
                if (isSelected) {
                    selectedIngredients.remove(ingredient);
                    listener.onIngredientChange(ingredient, false);
                } else {
                    selectedIngredients.add(ingredient);
                    listener.onIngredientChange(ingredient, true);
                }
                notifyItemChanged(position);
            });
        } else {
            Log.e("IngredientAdapter", "Ingredient at position " + position + " is null");
            holder.ingredientName.setText("Неизвестный ингредиент");
            holder.ingredientPrice.setText("0 ₽");
            holder.actionButton.setText("Добавить");
        }
    }

    @Override
    public int getItemCount() {
        return ingredients != null ? ingredients.size() : 0;
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

