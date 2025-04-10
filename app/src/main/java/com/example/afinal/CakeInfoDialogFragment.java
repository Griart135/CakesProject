package com.example.afinal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;

public class CakeInfoDialogFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_cake_info, container, false);

        TextView tvCakeInfo = view.findViewById(R.id.tv_cake_info);
        Button btnClose = view.findViewById(R.id.btn_close);

        Bundle args = getArguments();
        String cakeName = args != null ? args.getString("cakeName", "Unknown Cake") : "Unknown Cake";
        String[] ingredients = args != null ? args.getStringArray("ingredients") : new String[]{"No ingredients"};

        StringBuilder ingredientsList = new StringBuilder();
        for (String ingredient : ingredients) {
            ingredientsList.append("- ").append(ingredient).append("\n");
        }

        tvCakeInfo.setText(cakeName + "\n" +
                "- Weight: 1.2 kg\n" +
                "- Calories: 320 kcal/slice\n" +
                "- Allergens: Nuts, Dairy\n" +
                "Ingredients:\n" +
                ingredientsList.toString() +
                "- Baking Time: 2 hours\n" +
                "- Best served chilled");

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}

