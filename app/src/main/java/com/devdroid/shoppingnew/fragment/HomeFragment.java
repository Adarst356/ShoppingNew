package com.devdroid.shoppingnew.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;
import com.devdroid.shoppingnew.R;
import com.devdroid.shoppingnew.activity.CategoryActivity;
import com.devdroid.shoppingnew.activity.FilterBottomSheet;

import com.devdroid.shoppingnew.activity.SearchActivity;
import com.devdroid.shoppingnew.adapter.CategoryAdapter;
import com.devdroid.shoppingnew.adapter.ImageSliderAdapter;
import com.devdroid.shoppingnew.adapter.ProductAdapter;
import com.devdroid.shoppingnew.api.ApiClient;
import com.devdroid.shoppingnew.api.ApiInterface;
import com.devdroid.shoppingnew.modelclass.Category;
import com.devdroid.shoppingnew.modelclass.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private ViewPager2 imageSlider;
    private Handler sliderHandler = new Handler();
    private int currentPage = 0;

    private RecyclerView categoryRecyclerView, productRecyclerView;
    private LottieAnimationView productLottieLoader;
    boolean isLoadMore=true;
    boolean isLoading=true;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private List<Category> categoryList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();



    private ApiInterface apiInterface;

    private final ActivityResultLauncher<Intent> filterResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    int minPrice = result.getData().getIntExtra("min_price", 0);
                    int maxPrice = result.getData().getIntExtra("max_price", Integer.MAX_VALUE);
                    loadFilteredProducts(minPrice, maxPrice);
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        imageSlider = view.findViewById(R.id.imageSlider);
        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);
        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        productLottieLoader = view.findViewById(R.id.productLottieLoader);

        // Banner images
        List<Integer> banners = Arrays.asList(
                R.drawable.ic_vegi,
                R.drawable.ic_vegtable,
                R.drawable.ic_helathy,
                R.drawable.ic_store
        );
        imageSlider.setAdapter(new ImageSliderAdapter(banners));
        startAutoSlider();


        //Drawer Impelemention
        ImageView menuIcon = view.findViewById(R.id.menuIcon);

        menuIcon.setOnClickListener(v -> {
            DrawerLayout drawerLayout = getActivity().findViewById(R.id.main);
            if (drawerLayout != null) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        // Search click
        EditText searchEditText = view.findViewById(R.id.searchEditText);
        ImageView searchIcon = view.findViewById(R.id.searchIcon);

        view.findViewById(R.id.searchContainer).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SearchActivity.class));
        });
        searchEditText.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SearchActivity.class));
        });
        searchIcon.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SearchActivity.class));
        });


        // Filter click
        view.findViewById(R.id.filterIcon).setOnClickListener(v -> {
            FilterBottomSheet bottomSheet = new FilterBottomSheet();
            bottomSheet.setOnFilterApplyListener((min, max) -> {
                loadFilteredProducts(min, max); // Filter and reload products
            });
            bottomSheet.show(getChildFragmentManager(), "FilterBottomSheet");
        });

        // More Category click
        view.findViewById(R.id.more_category).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), CategoryActivity.class));
        });

        // Setup RecyclerViews
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        categoryRecyclerView.setAdapter(categoryAdapter);

        productRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productAdapter = new ProductAdapter(getContext(), productList);
        productRecyclerView.setAdapter(productAdapter);

        productRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1) && !isLoading && isLoadMore) {

                    fetchProducts();
                }
            }
        });
        // API
        apiInterface = ApiClient.getApiService();
        fetchCategories();
        fetchProducts();

        return view;
    }

    private void fetchCategories() {
        apiInterface.getAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList.clear();
                    categoryList.addAll(response.body());
                    categoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Failed to load categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProductsWithDelay() {
        productRecyclerView.setVisibility(View.GONE);
        productLottieLoader.setVisibility(View.VISIBLE);
        productLottieLoader.playAnimation();

        new Handler().postDelayed(this::fetchProducts, 1200);
    }

    private void fetchProducts() {
        isLoading=true;
        apiInterface.getAllProducts(productList.size(),20).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                productLottieLoader.cancelAnimation();
                productLottieLoader.setVisibility(View.GONE);
                productRecyclerView.setVisibility(View.VISIBLE);
                isLoading=false;
                if (response.isSuccessful() && response.body() != null) {
                    if(response.body().size()<20){
                        isLoadMore=false;
                    }
                    productList.addAll(response.body());
                    productAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Failed to load products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                productLottieLoader.cancelAnimation();
                productLottieLoader.setVisibility(View.GONE);
                productRecyclerView.setVisibility(View.VISIBLE);
                isLoading=false;
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFilteredProducts(int minPrice, int maxPrice) {
        productRecyclerView.setVisibility(View.GONE);
        productLottieLoader.setVisibility(View.VISIBLE);
        productLottieLoader.playAnimation();

        apiInterface.getFilteredProducts(minPrice, maxPrice, 0)
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                        productLottieLoader.cancelAnimation();
                        productLottieLoader.setVisibility(View.GONE);
                        productRecyclerView.setVisibility(View.VISIBLE);

                        if (response.isSuccessful() && response.body() != null) {
                            productList.clear();
                            productList.addAll(response.body());
                            productAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "No products in this range", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                        productLottieLoader.cancelAnimation();
                        productLottieLoader.setVisibility(View.GONE);
                        productRecyclerView.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "Filter failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void startAutoSlider() {
        sliderHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (imageSlider != null && imageSlider.getAdapter() != null) {
                    int pageCount = imageSlider.getAdapter().getItemCount();
                    imageSlider.setCurrentItem(currentPage % pageCount, true);
                    currentPage++;
                    sliderHandler.postDelayed(this, 2000);
                }
            }
        }, 800);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sliderHandler.removeCallbacksAndMessages(null);
    }
}
