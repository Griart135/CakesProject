package com.example.afinal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.fragment.app.DialogFragment;

public class FilterDialogFragment extends DialogFragment {

    public interface OnFilterApplyListener {
        void onFilterApply(String filter);
    }

    private OnFilterApplyListener listener;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_filter, null);

        builder.setView(view)
                .setTitle("Choose filter")
                .setPositiveButton("Apply", (dialog, id) -> {

                })
                .setNegativeButton("Cancel", (dialog, id) ->{

                });

        return builder.create();
    }
}