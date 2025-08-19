package com.devdroid.shoppingnew.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.devdroid.shoppingnew.R;
import com.devdroid.shoppingnew.api.ApiClient;
import com.devdroid.shoppingnew.api.ApiInterface;
import com.devdroid.shoppingnew.modelclass.LoginRequest;
import com.devdroid.shoppingnew.modelclass.LoginResponse;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    EditText emailInput, passwordInput;
    TextView loginButton, loginText;
    ImageView backArrow;
    TextInputLayout passwordLayout;
    CheckBox rememberCheckBox;

    ApiInterface apiInterface;
    final boolean[] isPasswordVisible = {false};
    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        loginText = findViewById(R.id.loginText);
        backArrow = findViewById(R.id.back_arrow);
        passwordLayout = findViewById(R.id.password_layout);
        rememberCheckBox = findViewById(R.id.remember_checkbox);

        // Initialize SharedPreferences
        prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        // Restore saved credentials if "remember me" was checked
        boolean isRemembered = prefs.getBoolean("rememberMe", false);
        if (isRemembered) {
            emailInput.setText(prefs.getString("savedEmail", ""));
            passwordInput.setText(prefs.getString("savedPassword", ""));
            rememberCheckBox.setChecked(true);
        }

        // Password toggle
        passwordLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
        passwordLayout.setEndIconDrawable(R.drawable.ic_eye_closed);
        passwordLayout.setEndIconOnClickListener(v -> {
            isPasswordVisible[0] = !isPasswordVisible[0];
            if (isPasswordVisible[0]) {
                passwordInput.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                passwordLayout.setEndIconDrawable(R.drawable.ic_eye_open);
            } else {
                passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordLayout.setEndIconDrawable(R.drawable.ic_eye_closed);
            }
            passwordInput.setSelection(passwordInput.getText().length());
        });

        apiInterface = ApiClient.getApiService();

        loginText.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, CreateAccountActivity.class));
        });

        backArrow.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, WelcomeActivity.class));
            finish();
        });

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (!validateInput(email, password)) return;

            loginButton.setEnabled(false);
            loginButton.setText("Logging in...");

            LoginRequest loginRequest = new LoginRequest(email, password);
            apiInterface.loginUser(loginRequest).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    loginButton.setEnabled(true);
                    loginButton.setText("Login");

                    if (response.isSuccessful() && response.body() != null) {

                        // Save session
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("isLoggedIn", true);

                        // Save credentials if Remember Me is checked
                        if (rememberCheckBox.isChecked()) {
                            editor.putBoolean("rememberMe", true);
                            editor.putString("savedEmail", email);
                            editor.putString("savedPassword", password);
                        } else {
                            editor.remove("rememberMe");
                            editor.remove("savedEmail");
                            editor.remove("savedPassword");
                        }

                        editor.apply();

                        Toast.makeText(SignInActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                        finishAffinity();
                    } else {
                        Toast.makeText(SignInActivity.this, "Login failed! Invalid credentials.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    loginButton.setEnabled(true);
                    loginButton.setText("Login");
                    Toast.makeText(SignInActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private boolean validateInput(String email, String password) {
        if (email.isEmpty()) {
            emailInput.setError("Email is required");
            emailInput.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Enter a valid email");
            emailInput.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            passwordInput.setError("Password is required");
            passwordInput.requestFocus();
            return false;
        }

        return true;
    }
}
