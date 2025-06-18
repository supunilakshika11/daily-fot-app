package com.example.dailyfot;

// Original imports
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

// Firebase imports
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * UserProfileActivity - This class manages the User Information screen
 *
 * WHAT THIS CLASS DOES:
 * 1. Shows user information (name, email) from Firebase
 * 2. Handles button clicks (Edit Info, Sign-out, Back arrow)
 * 3. Manages navigation to other screens
 * 4. Loads user data from Firebase Authentication
 */
public class UserProfileActivity extends AppCompatActivity {

    // UI components (connect to XML elements)
    private ImageView backArrow;        // Back arrow button
    private ImageView profilePicture;   // Profile picture (circular image)
    private ImageView cameraIcon;       // Camera icon for changing profile picture
    private TextView userName;          // Shows the user's name
    private TextView userEmail;         // Shows the user's email
    private Button editInfoButton;      // Blue "Edit Info" button
    private Button signoutButton;       // Red "Sign-out" button

    // User data variables
    private String currentUserName;     // Stores the user's name
    private String currentUserEmail;    // Stores the user's email

    // Firebase variables
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    /**
     * onCreate - This method runs when the screen first loads
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Check if user is logged in
        if (currentUser == null) {
            // User not logged in, redirect to login
            navigateToLogin();
            return;
        }

        // Connect Java variables to XML elements
        initializeViews();

        // Load user data from Firebase
        loadUserDataFromFirebase();

        // Set up what happens when buttons are clicked
        setupClickListeners();
    }

    /**
     * Connect Java variables to XML elements
     */
    private void initializeViews() {
        // Find each element by its ID and connect to Java variables
        backArrow = findViewById(R.id.back_arrow);
        profilePicture = findViewById(R.id.profile_picture);
        cameraIcon = findViewById(R.id.camera_icon);
        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        editInfoButton = findViewById(R.id.edit_info_button);
        signoutButton = findViewById(R.id.signout_button);
    }

    /**
     * Load user data from Firebase or Intent fallback
     */
    private void loadUserDataFromFirebase() {
        // First try to get data from intent (fallback)
        Intent intent = getIntent();
        currentUserName = intent.getStringExtra("username");

        if (currentUser != null) {
            // Use Firebase user data
            String email = currentUser.getEmail();
            if (email != null) {
                currentUserEmail = email;
                // Extract username from email if not provided in intent
                if (currentUserName == null || currentUserName.isEmpty()) {
                    currentUserName = email.contains("@") ? email.substring(0, email.indexOf("@")) : "User";
                }
            }

            // Try to load additional data from Firestore (optional)
            loadUserDataFromFirestore();
        } else {
            // Fallback to intent data if no Firebase user
            currentUserEmail = intent.getStringExtra("email");
        }

        // Set display values
        userName.setText(currentUserName != null ? currentUserName : "User");
        userEmail.setText(currentUserEmail != null ? currentUserEmail : "");
    }

    /**
     * Load additional user data from Firestore database
     */
    private void loadUserDataFromFirestore() {
        if (currentUser == null) return;

        db.collection("users").document(currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Document exists, load data from Firestore
                                String firestoreUsername = document.getString("username");
                                String firestoreEmail = document.getString("email");

                                if (firestoreUsername != null && !firestoreUsername.isEmpty()) {
                                    currentUserName = firestoreUsername;
                                    userName.setText(currentUserName);
                                }

                                if (firestoreEmail != null && !firestoreEmail.isEmpty()) {
                                    currentUserEmail = firestoreEmail;
                                    userEmail.setText(currentUserEmail);
                                }
                            }
                            // If document doesn't exist, keep the current display values
                        }
                        // If failed to load from Firestore, keep the current display values
                    }
                });
    }

    /**
     * Set up button click actions
     */
    private void setupClickListeners() {

        // BACK ARROW - Return to previous screen
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackClick();
            }
        });

        // CAMERA ICON - Change profile picture (future feature)
        cameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCameraClick();
            }
        });

        // EDIT INFO BUTTON - Edit user profile (future feature)
        editInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleEditInfoClick();
            }
        });

        // SIGN-OUT BUTTON - Log out and return to login screen
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignoutClick();
            }
        });
    }

    /**
     * BACK ARROW CLICK - Return to previous screen
     */
    private void handleBackClick() {
        // Close this screen and go back to the previous one
        finish();
    }

    /**
     * CAMERA ICON CLICK - Change profile picture
     */
    private void handleCameraClick() {
        Toast.makeText(this, "Profile picture change coming soon!", Toast.LENGTH_SHORT).show();

        // TODO FOR FUTURE: Add photo selection functionality
        // Example: Open gallery, crop image, save as profile picture to Firebase Storage
    }

    /**
     * EDIT INFO BUTTON CLICK - Edit user profile
     */
    private void handleEditInfoClick() {
        Toast.makeText(this, "Edit profile coming soon!", Toast.LENGTH_SHORT).show();

        // TODO FOR FUTURE: Navigate to edit profile screen
        // Example: Open EditProfileActivity where users can modify their info
    }

    /**
     * SIGN-OUT BUTTON CLICK - Log out user
     */
    private void handleSignoutClick() {
        // Show confirmation dialog before signing out
        new AlertDialog.Builder(this)
                .setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // User clicked "Yes" - perform sign out
                    performSignOut();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // User clicked "Cancel" - just close the dialog
                    dialog.dismiss();
                })
                .show();
    }

    /**
     * Perform Firebase sign out
     */
    private void performSignOut() {
        // Sign out from Firebase
        if (mAuth != null) {
            mAuth.signOut();
        }

        // Show success message
        Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show();

        // Navigate to login screen
        navigateToLogin();
    }

    /**
     * Navigate to login screen
     */
    private void navigateToLogin() {
        Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Update user information (for future use)
     */
    public void updateUserInfo(String name, String email) {
        if (name != null && !name.isEmpty()) {
            currentUserName = name;
            userName.setText(name);
        }

        if (email != null) {
            currentUserEmail = email;
            userEmail.setText(email.isEmpty() ? "" : email);
        }

        // TODO FOR FUTURE: Update data in Firestore as well
        // Map<String, Object> updates = new HashMap<>();
        // updates.put("username", name);
        // updates.put("email", email);
        // if (currentUser != null) {
        //     db.collection("users").document(currentUser.getUid()).update(updates);
        // }
    }

    /**
     * Check user authentication status when activity resumes
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Check if user is still logged in
        if (mAuth.getCurrentUser() == null) {
            // User was signed out elsewhere, redirect to login
            navigateToLogin();
        }
    }
}