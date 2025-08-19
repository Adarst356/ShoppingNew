package com.devdroid.shoppingnew.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.devdroid.shoppingnew.R;
import com.devdroid.shoppingnew.api.ApiClient;
import com.devdroid.shoppingnew.api.ApiInterface;
import com.devdroid.shoppingnew.modelclass.UserRequest;
import com.devdroid.shoppingnew.modelclass.UserResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccountActivity extends AppCompatActivity {

    TextInputEditText emailInput, nameInput, passwordInput;
    TextView signupButton, loginText;
    ImageView backArrow;
    LottieAnimationView signupLoader;
    TextInputLayout passwordLayout;
    ApiInterface apiInterface;

    boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Bind Views
        emailInput = findViewById(R.id.email_input);
        nameInput = findViewById(R.id.name_input);
        passwordInput = findViewById(R.id.password_input);
        signupButton = findViewById(R.id.signup_button);
        signupLoader = findViewById(R.id.signup_loader);
        loginText = findViewById(R.id.loginText);
        backArrow = findViewById(R.id.back_arrow);
        passwordLayout = findViewById(R.id.password_layout);

        // Setup password toggle eye icon
        passwordLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
        passwordLayout.setEndIconDrawable(R.drawable.ic_eye_closed);
        passwordLayout.setEndIconOnClickListener(v -> {
            isPasswordVisible = !isPasswordVisible;

            if (isPasswordVisible) {
                passwordInput.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                passwordLayout.setEndIconDrawable(R.drawable.ic_eye_open);
            } else {
                passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordLayout.setEndIconDrawable(R.drawable.ic_eye_closed);
            }

            passwordInput.setSelection(passwordInput.getText().length());
        });

        // Init Retrofit API
        apiInterface = ApiClient.getApiService();

        // Back to welcome
        backArrow.setOnClickListener(v -> {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
        });

        // Go to login
        loginText.setOnClickListener(v -> {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        });

        // Signup Click
        signupButton.setOnClickListener(v -> {
            signupButton.setVisibility(View.INVISIBLE);
            signupLoader.setVisibility(View.VISIBLE);
            validateAndRegister();
        });
    }

    private void validateAndRegister() {
        String email = emailInput.getText().toString().trim();
        String name = nameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();


        // Validate email
        if (email.isEmpty()) {
            emailInput.setError("Email is required");
            emailInput.requestFocus();
            showButtonAgain();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Enter a valid email");
            emailInput.requestFocus();
            showButtonAgain();
            return;
        }
// Validate name
        if (name.isEmpty()) {
            nameInput.setError("Name is required");
            nameInput.requestFocus();
            showButtonAgain();
            return;
        }

// Allow only alphabets and spaces
        if (!name.matches("^[a-zA-Z\\s]+$")) {
            nameInput.setError("Please enter a valid name");
            nameInput.requestFocus();
            showButtonAgain();
            return;
        }


        if (password.isEmpty()) {
            passwordLayout.setError("Password is required");
            passwordInput.requestFocus();
            showButtonAgain();
            return;
        }

        if (password.length() < 6) {
            passwordLayout.setError("Minimum 6 characters required");
            passwordInput.requestFocus();
            showButtonAgain();
            return;
        } else {
            passwordLayout.setError(null);
        }


        // Create User Request
        String avatarUrl = "https://i.imgur.com/yhW6Yw1.jpg";
        UserRequest userRequest = new UserRequest(avatarUrl, email, name, password);

        // Register API call
        apiInterface.registerUser(userRequest).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                showButtonAgain();
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(CreateAccountActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateAccountActivity.this, SignInActivity.class));
                    finish();
                } else {
                    Toast.makeText(CreateAccountActivity.this, "Registration failed. Try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                showButtonAgain();
                Toast.makeText(CreateAccountActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showButtonAgain() {
        signupLoader.setVisibility(View.GONE);
        signupButton.setVisibility(View.VISIBLE);
    }
}
