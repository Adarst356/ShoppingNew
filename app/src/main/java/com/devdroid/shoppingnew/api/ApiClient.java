package com.devdroid.shoppingnew.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://api.escuelajs.co/api/v1/";
    private static Retrofit retrofit;

    public static ApiInterface getApiService() {
        if (retrofit == null) {
            // Step 1: Create Logging Interceptor
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // Can be HEADERS, BASIC, NONE

            // Step 2: Optional - Add Header Interceptor (if you want to add headers like token)
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    // .addInterceptor(chain -> {
                    //     Request request = chain.request().newBuilder()
                    //         .addHeader("Authorization", "Bearer your_token_here")
                    //         .build();
                    //     return chain.proceed(request);
                    // })
                    .build();
            // Step 3: Add OkHttpClient to Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient) // Add client here
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiInterface.class);
    }
}

