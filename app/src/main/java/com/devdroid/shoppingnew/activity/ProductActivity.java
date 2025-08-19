package com.devdroid.shoppingnew.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devdroid.shoppingnew.R;
import com.devdroid.shoppingnew.adapter.ProductAdapter;
import com.devdroid.shoppingnew.api.ApiClient;
import com.devdroid.shoppingnew.api.ApiInterface;
import com.devdroid.shoppingnew.modelclass.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProduct;
    private ProductAdapter adapter;
    private ApiInterface apiInterface;
    private ImageView filterIcon;
    private TextView emptyView;


    private int offset = 0;
    private boolean isLoading = false;
    private boolean isLoadMore = true;
    private int categoryId = 0;

    private final List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Intent data
        categoryId = getIntent().getIntExtra("category_id", 0);
        String categoryTitle = getIntent().getStringExtra("category_title");

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (categoryTitle != null) toolbarTitle.setText(categoryTitle);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Views
        filterIcon = findViewById(R.id.filterIcon);
        recyclerViewProduct = findViewById(R.id.recyclerViewProduct);
        emptyView = findViewById(R.id.empty_view);

        recyclerViewProduct.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ProductAdapter(this, productList);
        recyclerViewProduct.setAdapter(adapter);

        apiInterface = ApiClient.getApiService();

        // Load first page
        loadProducts();


        recyclerViewProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                super.onScrolled(rv, dx, dy);
                if (!rv.canScrollVertically(1) && !isLoading && isLoadMore) {
                    loadProducts();
                }
            }
        });

        filterIcon.setOnClickListener(v -> {
            FilterBottomSheet bottomSheet = new FilterBottomSheet();
            bottomSheet.setOnFilterApplyListener((minPrice, maxPrice) -> {
                offset = 0;
                isLoadMore = true;
                productList.clear();
                adapter.notifyDataSetChanged();
                loadFilteredProducts(minPrice, maxPrice);
            });
            bottomSheet.show(getSupportFragmentManager(), "FilterBottomSheet");
        });
    }

    private void loadProducts() {
        isLoading = true;
        apiInterface.getCategoryProducts(categoryId,productList.size(), 6)
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        isLoading = false;
                        if (response.isSuccessful() && response.body() != null) {
                            List<Product> products = response.body();
                            productList.addAll(products);
                            adapter.notifyDataSetChanged();
                            toggleEmptyView(productList.isEmpty(), "No products found");
                            if (products.size() < 6)
                                isLoadMore = false;

                        } else {
                            toggleEmptyView(true, "No products found");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        isLoading = false;
                        Toast.makeText(ProductActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        toggleEmptyView(true, "Something went wrong");
                    }
                });
    }

    private void loadFilteredProducts(int minPrice, int maxPrice) {
        isLoading = true;
        apiInterface.getFilteredProducts(minPrice, maxPrice, categoryId)
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        isLoading = false;
                        if (response.isSuccessful() && response.body() != null) {
                            List<Product> products = response.body();
                            productList.clear();
                            productList.addAll(products);
                            adapter.notifyDataSetChanged();
                            toggleEmptyView(products.isEmpty(), "No products found in this range");
                        } else {
                            toggleEmptyView(true, "No products found in this range");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        isLoading = false;
                        Toast.makeText(ProductActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        toggleEmptyView(true, "Something went wrong");
                    }
                });
    }

    private void toggleEmptyView(boolean showEmpty, @Nullable String message) {
        if (showEmpty) {
            emptyView.setText(message != null ? message : "No products available");
            emptyView.setVisibility(TextView.VISIBLE);
            recyclerViewProduct.setVisibility(RecyclerView.GONE);
        } else {
            emptyView.setVisibility(TextView.GONE);
            recyclerViewProduct.setVisibility(RecyclerView.VISIBLE);
        }
    }
}
