package com.devdroid.shoppingnew.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devdroid.shoppingnew.R;
import com.devdroid.shoppingnew.modelclass.SplashItem;

import java.util.List;

public class SplashAdapter extends RecyclerView.Adapter<SplashAdapter.SplashViewHolder> {

    private final List<SplashItem> SplashItemList;

    public SplashAdapter(List<SplashItem> splashItemList) {
        this.SplashItemList = splashItemList;
    }

    @NonNull
    @Override
    public SplashViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_splash, parent, false);
        return new SplashViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SplashViewHolder holder, int position) {
        SplashItem item = SplashItemList.get(position);
        holder.image.setImageResource(item.getImageRes());
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return SplashItemList.size();
    }

    public static class SplashViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, description;

        public SplashViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
        }
    }
}