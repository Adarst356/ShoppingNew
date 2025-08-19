package com.devdroid.shoppingnew.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.devdroid.shoppingnew.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Back to splash
        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Create account
        LinearLayout createAccount = findViewById(R.id.createAccount);
        TextView textCreateAccount = findViewById(R.id.textCreateAccount);
        ImageView iconUser = findViewById(R.id.iconUser);
        createAccount.setOnClickListener(v -> {
            startActivity(new Intent(WelcomeActivity.this, CreateAccountActivity.class));
            finish();
        });

        // Login text
        TextView loginText = findViewById(R.id.loginText);
        loginText.setOnClickListener(v -> {
            startActivity(new Intent(WelcomeActivity.this, SignInActivity.class));
        });
    }
}
