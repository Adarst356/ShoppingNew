package com.devdroid.shoppingnew.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devdroid.shoppingnew.R;
import com.devdroid.shoppingnew.adapter.CategoryItemAdapter;
import com.devdroid.shoppingnew.api.ApiClient;
import com.devdroid.shoppingnew.api.ApiInterface;
import com.devdroid.shoppingnew.modelclass.Category;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private CategoryItemAdapter adapter;
    private List<Category> categoryList = new ArrayList<>();
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Back button logic
        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> finish());

        // UI references
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerViewCategories);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new CategoryItemAdapter(this, categoryList);
        recyclerView.setAdapter(adapter);

        // Retrofit
        apiInterface = ApiClient.getApiService();

        // Fetch data
        fetchCategoriesFromApi();
    }

    private void fetchCategoriesFromApi() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        apiInterface.getAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                if (response.isSuccessful() && response.body() != null) {
                    categoryList.clear();
                    categoryList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("CategoryActivity", "API Error: " + t.getMessage());
            }
        });
    }
}
