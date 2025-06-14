package com.example.dailyfot;

// These are imports - they tell Java what tools we need to use
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

/**
 * UserProfileActivity - This class manages the User Information screen
 *
 * WHAT THIS CLASS DOES:
 * 1. Shows user information (name, email)
 * 2. Handles button clicks (Edit Info, Sign-out, Back arrow)
 * 3. Manages navigation to other screens
 * 4. Loads user data passed from other activities
 */
public class UserProfileActivity extends AppCompatActivity {

    // STEP 1: Declare UI components (connect to XML elements)
    // These variables will connect to the views in your XML file
    private ImageView backArrow;        // Back arrow button
    private ImageView profilePicture;   // Profile picture (circular image)
    private ImageView cameraIcon;       // Camera icon for changing profile picture
    private TextView userName;          // Shows the user's name
    private TextView userEmail;         // Shows the user's email
    private Button editInfoButton;      // Blue "Edit Info" button
    private Button signoutButton;       // Red "Sign-out" button

    // STEP 2: Variables to store user data
    private String currentUserName;     // Stores the user's name
    private String currentUserEmail;    // Stores the user's email

    /**
     * onCreate - This method runs when the screen first loads
     * Think of it like the "setup" function for your screen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // STEP 3: Connect this Java file to the XML layout
        setContentView(R.layout.activity_user_profile);

        // STEP 4: Connect Java variables to XML elements
        initializeViews();

        // STEP 5: Load user data (name from login, email blank)
        loadUserData();

        // STEP 6: Set up what happens when buttons are clicked
        setupClickListeners();
    }

    /**
     * STEP 4 DETAILS: Connect Java variables to XML elements
     *
     * This method finds each element in your XML file and connects it
     * to a Java variable so you can control it with code
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
     * STEP 5 DETAILS: Load user data
     *
     * This method gets the username from the previous screen (MainActivity)
     * and sets up the email field as blank (as you requested)
     */
    private void loadUserData() {
        // Get data passed from the previous screen
        Intent intent = getIntent();

        // GET USERNAME (from login screen via MainActivity)
        currentUserName = intent.getStringExtra("username");
        if (currentUserName != null && !currentUserName.isEmpty()) {
            // If we have a username, show it
            userName.setText(currentUserName);
        } else {
            // If no username, show "User" as default
            userName.setText("User");
            currentUserName = "User";
        }

        // SET EMAIL AS BLANK (as you requested)
        // Email is not collected during registration, so it starts empty
        currentUserEmail = intent.getStringExtra("email");
        if (currentUserEmail != null && !currentUserEmail.isEmpty()) {
            userEmail.setText(currentUserEmail);
        } else {
            userEmail.setText(""); // Blank by default
            currentUserEmail = "";
        }
    }

    /**
     * STEP 6 DETAILS: Set up button click actions
     *
     * This method tells each button what to do when clicked
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
     *
     * For now, this just shows a message. In the future, you can add
     * functionality to let users select photos from gallery or take pictures
     */
    private void handleCameraClick() {
        Toast.makeText(this, "Profile picture change coming soon!", Toast.LENGTH_SHORT).show();

        // TODO FOR FUTURE: Add photo selection functionality
        // Example: Open gallery, crop image, save as profile picture
    }

    /**
     * EDIT INFO BUTTON CLICK - Edit user profile
     *
     * For now, this shows a message. In the future, you can create
     * an edit profile screen where users can change their name and email
     */
    private void handleEditInfoClick() {
        Toast.makeText(this, "Edit profile coming soon!", Toast.LENGTH_SHORT).show();

        // TODO FOR FUTURE: Navigate to edit profile screen
        // Example: Open EditProfileActivity where users can modify their info
    }

    /**
     * SIGN-OUT BUTTON CLICK - Log out user
     *
     * This shows a confirmation dialog, and if user confirms,
     * it logs them out and returns to the login screen
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
     * ACTUAL SIGN-OUT PROCESS
     *
     * This method handles the actual sign-out process:
     * 1. Shows success message
     * 2. Navigates back to login screen
     * 3. Clears the activity stack so user can't go back
     */
    private void performSignOut() {
        // Show success message
        Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show();

        // Navigate back to login screen
        Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);

        // Clear all previous activities so user can't go back after logout
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish(); // Close this activity
    }

    /**
     * PUBLIC METHOD: Update user information
     *
     * This method allows other activities to update the user information
     * displayed on this screen (useful for future edit profile functionality)
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
    }
}