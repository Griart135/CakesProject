package com.example.afinal;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ConfirmOrderDialogFragment extends DialogFragment {

    private static final String ARG_CAKE_NAME = "cake_name";
    private static final String ARG_HEIGHT = "height";
    private static final String ARG_RADIUS = "radius";
    private static final String ARG_SLICES = "slices";
    private static final String ARG_ADDRESS = "address";
    private OnConfirmListener listener;

    public interface OnConfirmListener {
        void onConfirm();
    }

    public static ConfirmOrderDialogFragment newInstance(String cakeName, int height, int radius, int slices, String address) {
        ConfirmOrderDialogFragment fragment = new ConfirmOrderDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CAKE_NAME, cakeName);
        args.putInt(ARG_HEIGHT, height);
        args.putInt(ARG_RADIUS, radius);
        args.putInt(ARG_SLICES, slices);
        args.putString(ARG_ADDRESS, address);
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext())
                .setBackground(getResources().getDrawable(R.drawable.dialog_background, null));

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_confirm_order, null);

        TextView detailsTextView = view.findViewById(R.id.order_details);
        Button confirmButton = view.findViewById(R.id.dialog_confirm_button);
        Button cancelButton = view.findViewById(R.id.dialog_cancel_button);

        Bundle args = getArguments();
        if (args != null) {
            String cakeName = args.getString(ARG_CAKE_NAME, "Unknown Cake");
            int height = args.getInt(ARG_HEIGHT, 0);
            int radius = args.getInt(ARG_RADIUS, 0);
            int slices = args.getInt(ARG_SLICES, 0);
            String address = args.getString(ARG_ADDRESS, "");
            detailsTextView.setText(String.format(
                    "Cake: %s\nHeight: %d cm\nRadius: %d cm\nSlices: %d\nAddress: %s",
                    cakeName, height, radius, slices, address));
        }

        confirmButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onConfirm();
            }
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());

        builder.setView(view);
        return builder.create();
    }
}
