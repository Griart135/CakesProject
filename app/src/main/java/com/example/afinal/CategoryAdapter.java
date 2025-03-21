package com.example.afinal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private String[] categoryItems;

    public CategoryAdapter(String[] categoryItems) {
        this.categoryItems = categoryItems;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.textView.setText(categoryItems[position]);
    }

    @Override
    public int getItemCount() {
        return categoryItems.length;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.categoryText);
        }
    }
}
