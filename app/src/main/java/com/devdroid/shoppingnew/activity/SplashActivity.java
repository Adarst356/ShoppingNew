package com.devdroid.shoppingnew.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;
import com.devdroid.shoppingnew.R;
import com.devdroid.shoppingnew.adapter.SplashAdapter;
import com.devdroid.shoppingnew.modelclass.SplashItem;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private LinearLayout dotsLayout;
    private TextView btnNext, btnSkip;
    private LottieAnimationView progressBar;

    private List<SplashItem> splashItemList;
    private SplashAdapter splashAdapter;

    private boolean isLoggedIn; // Login status

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //  Check login session
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        //  View references
        viewPager = findViewById(R.id.viewPager);
        dotsLayout = findViewById(R.id.dotsLayout);
        btnNext = findViewById(R.id.btn_next);
        btnSkip = findViewById(R.id.btn_skip);
        progressBar = findViewById(R.id.progressBar);

        //  Add onboarding slides
        splashItemList = new ArrayList<>();
        splashItemList.add(new SplashItem(R.drawable.ic_buy, "Buy Grocery", "Get your daily essentials delivered fast."));
        splashItemList.add(new SplashItem(R.drawable.ic_delevery, "Fast Delivery", "Fastest delivery within 15 mins."));
        splashItemList.add(new SplashItem(R.drawable.ic_food, "Enjoy Quality Food", "Only the best quality groceries for you."));

        splashAdapter = new SplashAdapter(splashItemList);
        viewPager.setAdapter(splashAdapter);

        setupIndicators();
        setCurrentIndicator(0);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                setCurrentIndicator(position);
                btnNext.setText(position == splashItemList.size() - 1 ? "Get Started" : "Next");
            }
        });



        //  Next or Get Started
        btnNext.setOnClickListener(v -> {
            int currentPos = viewPager.getCurrentItem();
            if (currentPos < splashItemList.size() - 1) {
                viewPager.setCurrentItem(currentPos + 1);
            } else {
                //  Show progress bar and delay
                viewPager.setVisibility(View.VISIBLE);
                findViewById(R.id.bottomLayout).setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                progressBar.postDelayed(this::navigateBasedOnLogin, 1500); // 1.5 sec
            }
        });

        // Skip button
        btnSkip.setOnClickListener(v -> navigateBasedOnLogin());
    }

    private void navigateBasedOnLogin() {
        if (isLoggedIn) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
        }
        finish();
    }

    private void setupIndicators() {
        ImageView[] indicators = new ImageView[splashItemList.size()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.indicator_inactive));
            indicators[i].setLayoutParams(layoutParams);
            dotsLayout.addView(indicators[i]);
        }
    }

    private void setCurrentIndicator(int index) {
        int count = dotsLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            ImageView dot = (ImageView) dotsLayout.getChildAt(i);
            int drawableId = (i == index)
                    ? R.drawable.indicator_active
                    : R.drawable.indicator_inactive;
            dot.setImageDrawable(ContextCompat.getDrawable(this, drawableId));
        }
    }
}
