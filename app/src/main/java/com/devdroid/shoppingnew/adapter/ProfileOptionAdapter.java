package com.devdroid.shoppingnew.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devdroid.shoppingnew.R;
import com.devdroid.shoppingnew.modelclass.ProfileOption;

import java.util.List;

public class ProfileOptionAdapter extends RecyclerView.Adapter<ProfileOptionAdapter.ViewHolder> {

    private final List<ProfileOption> optionList;
    private final OnOptionClickListener listener;

    // Constructor with click listener
    public ProfileOptionAdapter(List<ProfileOption> optionList, OnOptionClickListener listener) {
        this.optionList = optionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProfileOptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile_option, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileOptionAdapter.ViewHolder holder, int position) {
        ProfileOption option = optionList.get(position);
        holder.icon.setImageResource(option.getIconRes());
        holder.title.setText(option.getTitle());

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOptionClick(option);
            }
        });
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
        }
    }

    // Interface for click callback
    public interface OnOptionClickListener {
        void onOptionClick(ProfileOption option);
    }
}
