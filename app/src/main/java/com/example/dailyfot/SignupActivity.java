package com.example.dailyfot;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    // UI components
    private EditText usernameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText reenterPasswordInput;
    private Button signupButton;
    private TextView loginLink;
    private ImageView passwordToggle;
    private ImageView reenterPasswordToggle;

    // Track password visibility states
    private boolean isPasswordVisible = false;
    private boolean isReenterPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

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
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        reenterPasswordInput = findViewById(R.id.reenter_password_input);
        signupButton = findViewById(R.id.signup_button);
        loginLink = findViewById(R.id.login_link);
        passwordToggle = findViewById(R.id.password_toggle);
        reenterPasswordToggle = findViewById(R.id.reenter_password_toggle);
    }

    /**
     * Set up click event handlers
     */
    private void setupClickListeners() {
        // Sign up button click
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignup();
            }
        });

        // Login link click
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLoginClick();
            }
        });

        // Password toggle click
        passwordToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        // Reenter password toggle click
        reenterPasswordToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleReenterPasswordVisibility();
            }
        });
    }

    /**
     * Toggle password visibility for main password field
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
     * Toggle password visibility for reenter password field
     */
    private void toggleReenterPasswordVisibility() {
        if (isReenterPasswordVisible) {
            // Hide password
            reenterPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            reenterPasswordToggle.setImageResource(R.drawable.ic_eye_off_spec);
            isReenterPasswordVisible = false;
        } else {
            // Show password
            reenterPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            reenterPasswordToggle.setImageResource(R.drawable.ic_eye_on_spec);
            isReenterPasswordVisible = true;
        }

        // Move cursor to end of text
        reenterPasswordInput.setSelection(reenterPasswordInput.getText().length());
    }

    /**
     * Handle signup button click with comprehensive validation
     */
    private void handleSignup() {
        // Get text from input fields and remove extra spaces
        String username = usernameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String reenterPassword = reenterPasswordInput.getText().toString().trim();

        // Validate all inputs
        if (!validateInputs(username, email, password, reenterPassword)) {
            return; // Stop if validation fails
        }

        // For demo purposes - show success message
        Toast.makeText(this, "Account created successfully! Welcome " + username, Toast.LENGTH_SHORT).show();

        // Navigate back to login screen
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Close signup screen

        // TODO: Add real user registration here
        // - Send data to backend/database
        // - Handle registration errors
        // - Maybe send verification email
    }

    /**
     * Comprehensive input validation
     */
    private boolean validateInputs(String username, String email, String password, String reenterPassword) {

        // Username validation
        if (username.isEmpty()) {
            usernameInput.setError("Username is required");
            usernameInput.requestFocus();
            return false;
        }

        if (username.length() < 3) {
            usernameInput.setError("Username must be at least 3 characters");
            usernameInput.requestFocus();
            return false;
        }

        // Email validation
        if (email.isEmpty()) {
            emailInput.setError("Email is required");
            emailInput.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Please enter a valid email address");
            emailInput.requestFocus();
            return false;
        }

        // Password validation
        if (password.isEmpty()) {
            passwordInput.setError("Password is required");
            passwordInput.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            passwordInput.requestFocus();
            return false;
        }

        // Reenter password validation
        if (reenterPassword.isEmpty()) {
            reenterPasswordInput.setError("Please confirm your password");
            reenterPasswordInput.requestFocus();
            return false;
        }

        if (!password.equals(reenterPassword)) {
            reenterPasswordInput.setError("Passwords do not match");
            reenterPasswordInput.requestFocus();
            return false;
        }

        return true; // All validation passed
    }

    /**
     * Handle login link click - go back to login screen
     */
    private void handleLoginClick() {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Close signup screen
    }
}
