package com.example.dailyfot; // Replace with your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.widget.RelativeLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // UI components
    private ImageView backArrow;
    private TextView greetingText;
    private ImageView userProfileIcon;
    private ImageView notificationIcon;
    private ImageView menuIcon;
    private EditText searchInput;
    private ImageView searchIcon;
    private RelativeLayout newsCard1;
    private RelativeLayout newsCard2;
    private RelativeLayout newsCard3;
    private LinearLayout sportTab;
    private LinearLayout educationTab;
    private LinearLayout eventTab;

    // Navigation Drawer components
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    // Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        initializeViews();

        // Set up navigation drawer
        setupNavigationDrawer();

        // Set up click listeners
        setupClickListeners();

        // Set greeting text (you can customize this)
        setUserGreeting();
    }

    /**
     * Connect Java variables to XML elements
     */
    private void initializeViews() {
        backArrow = findViewById(R.id.back_arrow);
        greetingText = findViewById(R.id.greeting_text);
        userProfileIcon = findViewById(R.id.user_profile_icon);
        notificationIcon = findViewById(R.id.notification_icon);
        menuIcon = findViewById(R.id.menu_icon);
        searchInput = findViewById(R.id.search_input);
        searchIcon = findViewById(R.id.search_icon);
        newsCard1 = findViewById(R.id.news_card_1);
        newsCard2 = findViewById(R.id.news_card_2);
        newsCard3 = findViewById(R.id.news_card_3);
        sportTab = findViewById(R.id.sport_tab);
        educationTab = findViewById(R.id.education_tab);
        eventTab = findViewById(R.id.event_tab);

        // Navigation Drawer components
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Set up navigation drawer functionality
     */
    private void setupNavigationDrawer() {
        // Set up navigation item selected listener
        navigationView.setNavigationItemSelectedListener(this);

        // Update navigation header with user info
        updateNavigationHeader();
    }

    /**
     * Update navigation header with user information
     */
    private void updateNavigationHeader() {
        View headerView = navigationView.getHeaderView(0);
        TextView navHeaderName = headerView.findViewById(R.id.nav_header_name);

        // Get username from intent or use default
        Intent intent = getIntent();
        String username = "User";
        if (intent != null && intent.getStringExtra("username") != null) {
            username = intent.getStringExtra("username");
        }

        // Only set the name, no fake email
        navHeaderName.setText(username);
    }

    /**
     * Handle navigation menu item selections
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            // Already on home screen
            Toast.makeText(this, "You're already on Home", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_profile) {
            handleUserProfileClick();
        } else if (itemId == R.id.nav_developer) {
            handleDeveloperInfoClick();
        } else if (itemId == R.id.nav_settings) {
            handleSettingsClick();
        } else if (itemId == R.id.nav_sign_out) {
            handleSignOutClick();
        }

        // Close drawer after selection
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Set up click event handlers
     */
    private void setupClickListeners() {
        // Back arrow click - go back to login
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackClick();
            }
        });

        // User profile icon click
        userProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUserProfileClick();
            }
        });

        // Notification icon click
        notificationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNotificationClick();
            }
        });

        // Menu icon click - Open navigation drawer
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleMenuClick();
            }
        });

        // Search icon click
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSearchClick();
            }
        });

        // News card clicks
        newsCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNewsCardClick("Noor Feast Event", "A blessed celebration of tradition and community bringing together flavors, culture, and joy in a magnificent feast.");
            }
        });

        newsCard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNewsCardClick("International Dance Day", "A global celebration of the art of dance and its power to express what words often can't. From grooving to storytelling, dance unites generations and cultures.");
            }
        });

        newsCard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNewsCardClick("Sinhala Event 2025", "A cultural celebration showcasing the rich heritage and traditions of Sri Lankan culture with music, dance, and traditional arts.");
            }
        });

        // Bottom navigation clicks
        sportTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTabClick("Sport");
            }
        });

        educationTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTabClick("Education");
            }
        });

        eventTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTabClick("Event");
            }
        });
    }

    /**
     * Handle back button press
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Handle menu icon click - Open navigation drawer
     */
    private void handleMenuClick() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    /**
     * Handle developer info click
     */
    private void handleDeveloperInfoClick() {
        Intent intent = new Intent(MainActivity.this, DeveloperInfoActivity.class);
        startActivity(intent);
    }

    /**
     * Handle settings click
     */
    private void handleSettingsClick() {
        Toast.makeText(this, "Settings screen coming soon!", Toast.LENGTH_SHORT).show();

        // TODO: Create SettingsActivity
        // Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        // startActivity(intent);
    }

    /**
     * Handle sign out click - SAME AS UserProfileActivity
     */
    private void handleSignOutClick() {
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
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Set greeting text with username from login
     */
    private void setUserGreeting() {
        // Get username passed from LoginActivity
        Intent intent = getIntent();
        if (intent != null) {
            String username = intent.getStringExtra("username");
            if (username != null && !username.isEmpty()) {
                greetingText.setText("Hi, " + username);
                return;
            }
        }

        // Default greeting if no username provided
        greetingText.setText("Hi, User");
    }

    /**
     * Handle back arrow click - return to login screen
     */
    private void handleBackClick() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Close current activity
    }

    /**
     * Handle user profile icon click
     */
    private void handleUserProfileClick() {
        // Create Intent to navigate to User Information screen
        Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);

        // Extract username from greeting text
        String username = greetingText.getText().toString();
        if (username.startsWith("Hi, ")) {
            username = username.substring(4); // Remove "Hi, " prefix
        }

        // Pass username to User Information screen
        intent.putExtra("username", username);

        // Open the User Information screen
        startActivity(intent);
    }

    /**
     * Handle notification icon click
     */
    private void handleNotificationClick() {
        Toast.makeText(this, "Notifications screen coming soon!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Handle search functionality
     */
    private void handleSearchClick() {
        String searchQuery = searchInput.getText().toString().trim();
        if (searchQuery.isEmpty()) {
            Toast.makeText(this, "Please enter something to search", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Searching for: " + searchQuery, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Handle news card clicks - Navigate to EventNewsActivity
     */
    private void handleNewsCardClick(String newsTitle, String newsDescription) {
        Intent intent = new Intent(MainActivity.this, EventNewsActivity.class);
        intent.putExtra("event_title", newsTitle);
        intent.putExtra("event_description", newsDescription);
        startActivity(intent);
    }

    /**
     * Handle bottom navigation tab clicks
     */
    private void handleTabClick(String tabName) {
        Toast.makeText(this, tabName + " section coming soon!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Method to update greeting text with username
     */
    public void setUserGreeting(String username) {
        if (username != null && !username.isEmpty()) {
            greetingText.setText("Hi, " + username);
        }
    }
}