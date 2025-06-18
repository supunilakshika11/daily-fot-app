package com.example.dailyfot;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

// Firebase imports
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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
    private ProgressBar progressBar;

    // Track password visibility states
    private boolean isPasswordVisible = false;
    private boolean isReenterPasswordVisible = false;

    // Firebase variables
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

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
        progressBar = findViewById(R.id.progress_bar); // Add to XML if not exists

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Hide progress bar initially
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
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
     * Handle signup button click with Firebase authentication
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

        // Show loading indicator
        showLoading(true);

        // Create user account with Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration successful - save user data
                            FirebaseUser user = mAuth.getCurrentUser();
                            saveUserData(user.getUid(), username, email);
                        } else {
                            // Registration failed
                            showLoading(false);
                            String errorMessage = getFirebaseErrorMessage(task.getException());
                            Toast.makeText(SignupActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * Save user data to Firestore
     */
    private void saveUserData(String userId, String username, String email) {
        // Create user data map
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", username);
        userData.put("email", email);
        userData.put("profilePicture", ""); // Empty by default
        userData.put("createdAt", System.currentTimeMillis());

        // Save to Firestore
        db.collection("users").document(userId)
                .set(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        showLoading(false);

                        if (task.isSuccessful()) {
                            // Data saved successfully
                            Toast.makeText(SignupActivity.this, "Account created successfully! Welcome " + username, Toast.LENGTH_SHORT).show();
                            navigateToMainActivity(username);
                        } else {
                            // Failed to save user data, but account was created
                            Toast.makeText(SignupActivity.this, "Account created but failed to save profile. Please try logging in.", Toast.LENGTH_LONG).show();
                            navigateToLogin();
                        }
                    }
                });
    }

    /**
     * Show or hide loading indicator
     */
    private void showLoading(boolean isLoading) {
        if (isLoading) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            signupButton.setEnabled(false);
            signupButton.setText("Creating Account...");
        } else {
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
            signupButton.setEnabled(true);
            signupButton.setText("Sign Up");
        }
    }

    /**
     * Get user-friendly Firebase error message
     */
    private String getFirebaseErrorMessage(Exception exception) {
        if (exception == null) {
            return "Registration failed. Please try again.";
        }

        String message = exception.getMessage();
        if (message != null) {
            if (message.contains("email address is already in use")) {
                return "An account with this email already exists. Please use a different email or try logging in.";
            } else if (message.contains("email address is badly formatted")) {
                return "Please enter a valid email address.";
            } else if (message.contains("password is too weak")) {
                return "Password is too weak. Please choose a stronger password.";
            } else if (message.contains("network error")) {
                return "Network error. Please check your internet connection.";
            }
        }

        return "Registration failed. Please try again.";
    }

    /**
     * Navigate to MainActivity after successful registration
     */
    private void navigateToMainActivity(String username) {
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        intent.putExtra("username", username);

        // Clear activity stack
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish();
    }

    /**
     * Navigate to login activity
     */
    private void navigateToLogin() {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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

    /**
     * Clear error messages when activity starts
     */
    @Override
    protected void onStart() {
        super.onStart();
        usernameInput.setError(null);
        emailInput.setError(null);
        passwordInput.setError(null);
        reenterPasswordInput.setError(null);
    }
}