package com.devdroid.shoppingnew.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.devdroid.shoppingnew.R;

public class ProductDetailsActivity extends AppCompatActivity {

    ImageView image;
    TextView title, subtitle, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        image = findViewById(R.id.img_lime);
        title = findViewById(R.id.product_name);
        subtitle = findViewById(R.id.product_weight);
        price = findViewById(R.id.product_price);

        // Get data from Intent
        String imageUrl = getIntent().getStringExtra("image_url");
        String productTitle = getIntent().getStringExtra("title");
        String productSubtitle = getIntent().getStringExtra("subtitle");
        String productPrice = getIntent().getStringExtra("price");

        // Set data
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_fruits1)
                    .into(image);
        } else {
            image.setImageResource(R.drawable.ic_fruits1);
        }

        title.setText(productTitle);
        subtitle.setText(productSubtitle);
        price.setText(productPrice);

        // Back arrow
        ImageView backBtn = findViewById(R.id.icon_arrow);
        backBtn.setOnClickListener(v -> onBackPressed());

        // Quantity management
        ImageView btnPlus = findViewById(R.id.btn_plus);
        ImageView btnMinus = findViewById(R.id.btn_minus);
        TextView tvQuantity = findViewById(R.id.tv_quantity);

        int[] quantity = {1};
        tvQuantity.setText(String.valueOf(quantity[0]));

        btnPlus.setOnClickListener(v -> {
            quantity[0]++;
            tvQuantity.setText(String.valueOf(quantity[0]));
        });

        btnMinus.setOnClickListener(v -> {
            if (quantity[0] > 1) {
                quantity[0]--;
                tvQuantity.setText(String.valueOf(quantity[0]));
            }
        });
    }
}
