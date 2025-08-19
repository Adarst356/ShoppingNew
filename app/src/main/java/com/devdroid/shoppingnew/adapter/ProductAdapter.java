package com.devdroid.shoppingnew.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devdroid.shoppingnew.R;
import com.devdroid.shoppingnew.activity.ProductDetailsActivity;
import com.devdroid.shoppingnew.modelclass.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public void setProductList(List<Product> updatedList) {
        this.productList = updatedList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        // Set product data
        holder.title.setText(product.getTitle());
        holder.subtitle.setText(product.getDescription());
        holder.price.setText("₹ " + product.getPrice());

        // Load product image
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            Glide.with(context)
                    .load(product.getImages().get(0))
                    .placeholder(R.drawable.no_image_available)
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.no_image_available);
        }

        // Add to Cart Button Click
        holder.addToCart.setOnClickListener(v -> {
            holder.addToCart.setText("Added");
            Toast.makeText(context, "Added to cart: " + product.getTitle(), Toast.LENGTH_SHORT).show();
        });

        // Favorite icon click
        holder.favoriteIcon.setOnClickListener(v -> {
            Toast.makeText(context, "Added to favorites: " + product.getTitle(), Toast.LENGTH_SHORT).show();
        });

        // Product click - open details activity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("title", product.getTitle());
            intent.putExtra("subtitle", product.getDescription());
            intent.putExtra("price", "₹ " + product.getPrice());
            if (product.getImages() != null && !product.getImages().isEmpty()) {
                intent.putExtra("image_url", product.getImages().get(0));
            }
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
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
