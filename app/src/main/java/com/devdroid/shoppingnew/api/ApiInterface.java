package com.devdroid.shoppingnew.api;

import com.devdroid.shoppingnew.modelclass.Category;

import com.devdroid.shoppingnew.modelclass.LoginRequest;
import com.devdroid.shoppingnew.modelclass.LoginResponse;
import com.devdroid.shoppingnew.modelclass.Product;
import com.devdroid.shoppingnew.modelclass.UserRequest;
import com.devdroid.shoppingnew.modelclass.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {


    @POST("auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("users")
    Call<UserResponse> registerUser(@Body UserRequest userRequest);

    @GET("categories")
    Call<List<Category>> getAllCategories();

    @GET("products")
    Call<List<Product>> getAllProducts(@Query("offset") int offset,@Query("limit") int limit);

    @GET("products")
    Call<List<Product>> getCategoryProducts(
            @Query("categoryId") int categoryId,
            @Query("offset") int offset,
            @Query("limit") int limit
    );
    @GET("products")
    Call<List<Product>> getFilteredProducts(
            @Query("price_min") int minPrice,
            @Query("price_max") int maxPrice,
            @Query("categoryId") int categoryId);


}
