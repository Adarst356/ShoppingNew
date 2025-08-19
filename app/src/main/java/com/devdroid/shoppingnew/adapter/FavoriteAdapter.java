package com.devdroid.shoppingnew.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devdroid.shoppingnew.R;
import com.devdroid.shoppingnew.modelclass.Favorite;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private Context context;
    private List<Favorite> favoriteList;
    private OnFavoriteChangeListener listener;

    public interface OnFavoriteChangeListener {
        void onQuantityZero(int position);
        void onItemLongPressed(int position);
    }

    public FavoriteAdapter(Context context, List<Favorite> favoriteList, OnFavoriteChangeListener listener) {
        this.context = context;
        this.favoriteList = favoriteList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorite_item, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Favorite model = favoriteList.get(position);

        holder.imgProduct.setImageResource(model.getImageResId());
        holder.tvPrice.setText(model.getPrice());
        holder.tvTitle.setText(model.getTitle());
        holder.tvWeight.setText(model.getWeight());
        holder.tvQuantity.setText(String.valueOf(model.getQuantity()));

        // + button logic
        holder.btnPlus.setOnClickListener(v -> {
            int qty = model.getQuantity();
            qty++;
            model.setQuantity(qty);
            holder.tvQuantity.setText(String.valueOf(qty));
        });

        // - button logic
        holder.btnMinus.setOnClickListener(v -> {
            int qty = model.getQuantity();
            if (qty > 0) {
                qty--;
                model.setQuantity(qty);
                holder.tvQuantity.setText(String.valueOf(qty));

                if (qty == 0 && listener != null) {
                    listener.onQuantityZero(holder.getAdapterPosition());
                }
            }
        });

        // Long press to remove item
        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onItemLongPressed(holder.getAdapterPosition());
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvPrice, tvTitle, tvWeight, tvQuantity;
        ImageView btnPlus, btnMinus;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_product);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvWeight = itemView.findViewById(R.id.tv_weight);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            btnPlus = itemView.findViewById(R.id.btn_plus);
            btnMinus = itemView.findViewById(R.id.btn_minus);
        }
    }
}
