package com.devdroid.shoppingnew.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.devdroid.shoppingnew.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FilterBottomSheet extends BottomSheetDialogFragment {

    private EditText minPriceEditText, maxPriceEditText;
    private TextView applyButton;
    private LottieAnimationView lottieProgress;
    private ImageView ivBack, ivReset;

    private OnFilterApplyListener listener;

    public interface OnFilterApplyListener {
        void onFilterApplied(int minPrice, int maxPrice);
    }

    public void setOnFilterApplyListener(OnFilterApplyListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_filter, container, false);

        minPriceEditText = view.findViewById(R.id.min_price);
        maxPriceEditText = view.findViewById(R.id.max_price);
        applyButton = view.findViewById(R.id.btn_apply);
        lottieProgress = view.findViewById(R.id.lottieProgress);
        ivBack = view.findViewById(R.id.iv_back);
        ivReset = view.findViewById(R.id.iv_reset);

        // Back closes the dialog
        ivBack.setOnClickListener(v -> dismiss());

        // Reset prices
        ivReset.setOnClickListener(v -> {
            minPriceEditText.setText("");
            maxPriceEditText.setText("");
        });

        applyButton.setOnClickListener(v -> {
            String minText = minPriceEditText.getText().toString().trim();
            String maxText = maxPriceEditText.getText().toString().trim();

            if (TextUtils.isEmpty(minText) || TextUtils.isEmpty(maxText)) {
                Toast.makeText(getContext(), "Please enter both min and max price", Toast.LENGTH_SHORT).show();
                return;
            }

            int minPrice = Integer.parseInt(minText);
            int maxPrice = Integer.parseInt(maxText);

            if (listener != null) {
                lottieProgress.setVisibility(View.VISIBLE);
                applyButton.setText("");
                listener.onFilterApplied(minPrice, maxPrice);
                dismiss();
            }
        });

        return view;
    }
}
