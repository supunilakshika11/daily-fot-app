package com.example.dailyfot;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    // UI components
    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;
    private TextView signupLink;
    private ImageView passwordToggle;
    private ProgressBar progressBar;

    // Track password visibility state
    private boolean isPasswordVisible = false;

    // Firebase Authentication
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components
        initializeViews();

        // Set up click listeners
        setupClickListeners();

        // Check if user is already logged in
        checkCurrentUser();
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
        progressBar = findViewById(R.id.progress_bar); // Add to XML if missing

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
     * Check if user is already logged in
     */
    private void checkCurrentUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already logged in, extract username from email
            String email = currentUser.getEmail();
            String username = email;
            if (email != null && email.contains("@")) {
                username = email.substring(0, email.indexOf("@"));
            }

            // Navigate to MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
            finish();
        }
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
     * Handle login button click with Firebase authentication
     * Supports both username and email login
     */
    private void handleLogin() {
        // Get text from input fields and remove extra spaces
        String usernameOrEmail = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validate inputs
        if (usernameOrEmail.isEmpty()) {
            usernameInput.setError("Username or Email is required");
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

        // Show loading indicator
        showLoading(true);

        // Check if input is email or username
        if (usernameOrEmail.contains("@")) {
            // Input is email, login directly
            loginWithEmail(usernameOrEmail, password, usernameOrEmail);
        } else {
            // Input is username, find email first
            findEmailByUsername(usernameOrEmail, password);
        }
    }

    /**
     * Find email address by username in Firestore
     */
    private void findEmailByUsername(String username, String password) {
        db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            // Username found, get the email
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String email = document.getString("email");
                                if (email != null) {
                                    // Login with found email
                                    loginWithEmail(email, password, username);
                                    return;
                                }
                            }
                            // Email not found in document
                            showLoading(false);
                            Toast.makeText(LoginActivity.this, "Account not found. Please check your username or register.", Toast.LENGTH_LONG).show();
                        } else {
                            // Username not found
                            showLoading(false);
                            Toast.makeText(LoginActivity.this, "Username not found. Please check your username or register.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * Login with email using Firebase Authentication
     */
    private void loginWithEmail(String email, String password, String displayUsername) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        showLoading(false);

                        if (task.isSuccessful()) {
                            // Login successful
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(LoginActivity.this, "Login successful! Welcome " + displayUsername, Toast.LENGTH_SHORT).show();

                            // Navigate to main news screen
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("username", displayUsername);
                            startActivity(intent);
                            finish(); // Close login screen so user can't go back
                        } else {
                            // Login failed
                            String errorMessage = getFirebaseErrorMessage(task.getException());
                            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * Get user-friendly Firebase error message
     */
    private String getFirebaseErrorMessage(Exception exception) {
        if (exception == null) {
            return "Login failed. Please try again.";
        }

        String message = exception.getMessage();
        if (message != null) {
            if (message.contains("password is invalid")) {
                return "Incorrect password. Please try again.";
            } else if (message.contains("no user record")) {
                return "No account found with this email. Please register first.";
            } else if (message.contains("email address is badly formatted")) {
                return "Please enter a valid email address.";
            } else if (message.contains("network error")) {
                return "Network error. Please check your internet connection.";
            } else if (message.contains("too many requests")) {
                return "Too many failed attempts. Please try again later.";
            }
        }

        return "Login failed. Please check your credentials and try again.";
    }

    /**
     * Show or hide loading indicator
     */
    private void showLoading(boolean isLoading) {
        if (isLoading) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            loginButton.setEnabled(false);
            loginButton.setText("Logging in...");
        } else {
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
            loginButton.setEnabled(true);
            loginButton.setText("Login");
        }
    }

    /**
     * Handle sign up link click - Navigate to SignupActivity
     */
    private void handleSignupClick() {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }

    /**
     * Called when activity starts - clear any error messages
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Clear error messages when activity starts
        usernameInput.setError(null);
        passwordInput.setError(null);
    }
}