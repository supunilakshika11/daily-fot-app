package com.example.dailyfot;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    // UI components
    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;
    private TextView signupLink;
    private ImageView passwordToggle;

    // Track password visibility state
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components
        initializeViews();

        // Set up click listeners
        setupClickListeners();
    }

    /**
     * Connect Java variables to XML elements
     */
    private void initializeViews() {
        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        signupLink = findViewById(R.id.signup_link);
        passwordToggle = findViewById(R.id.password_toggle);
    }

    /**
     * Set up click event handlers
     */
    private void setupClickListeners() {
        // Login button click
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        // Sign up link click
        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignupClick();
            }
        });

        // Password toggle click
        passwordToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });
    }

    /**
     * Toggle password visibility (show/hide password)
     */
    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordToggle.setImageResource(R.drawable.ic_eye_off_spec);
            isPasswordVisible = false;
        } else {
            // Show password
            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordToggle.setImageResource(R.drawable.ic_eye_on_spec);
            isPasswordVisible = true;
        }

        // Move cursor to end of text
        passwordInput.setSelection(passwordInput.getText().length());
    }

    /**
     * Handle login button click with validation
     */
    private void handleLogin() {
        // Get text from input fields and remove extra spaces
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validate inputs
        if (username.isEmpty()) {
            usernameInput.setError("Username is required");
            usernameInput.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordInput.setError("Password is required");
            passwordInput.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            passwordInput.requestFocus();
            return;
        }

        // Show success message
        Toast.makeText(this, "Login successful! Welcome " + username, Toast.LENGTH_SHORT).show();

        // Navigate to main news screen
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("username", username); // Pass username to MainActivity
        startActivity(intent);
        finish(); // Close login screen so user can't go back with back button
    }

    /**
     * Handle sign up link click - Navigate to SignupActivity
     */
    private void handleSignupClick() {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }
}