package com.devdroid.shoppingnew.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devdroid.shoppingnew.R;
import com.devdroid.shoppingnew.activity.ProductDetailsActivity;
import com.devdroid.shoppingnew.modelclass.Product;

import java.util.List;

public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ProductViewHolder> {

    private List<Product> productList;

    public ProductItemAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.title.setText(product.getTitle());
        holder.subtitle.setText(product.getDescription());
        holder.price.setText("$" + product.getPrice());


        if (product.getImages() != null && !product.getImages().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(product.getImages().get(0))
                    .placeholder(R.drawable.grapes)
                    .into(holder.image);
        }


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ProductDetailsActivity.class);
            intent.putExtra("image_url", product.getImages().get(0));
            intent.putExtra("price", product.getPrice());
            intent.putExtra("title", product.getTitle());
            intent.putExtra("subtitle", product.getDescription());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView image, favoriteIcon;
        TextView price, title, subtitle, addToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            favoriteIcon = itemView.findViewById(R.id.favorite);
            image = itemView.findViewById(R.id.fruits);
            price = itemView.findViewById(R.id.price);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            addToCart = itemView.findViewById(R.id.add_to_Cart);
        }
    }
}
