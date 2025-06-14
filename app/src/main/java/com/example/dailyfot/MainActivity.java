package com.example.dailyfot; // Replace with your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.RelativeLayout;


public class MainActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        initializeViews();

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

        // Menu icon click
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
     *
     * WHAT THIS DOES:
     * 1. Creates an Intent (like a message) to open the UserProfileActivity
     * 2. Extracts the username from the greeting text ("Hi, Username")
     * 3. Passes the username to the User Information screen
     * 4. Starts the new activity (opens the User Information screen)
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

        // TODO: Navigate to notifications screen
        // Intent intent = new Intent(MainActivity.this, NotificationsActivity.class);
        // startActivity(intent);
    }

    /**
     * Handle menu icon click
     */
    private void handleMenuClick() {
        Toast.makeText(this, "Menu opened!", Toast.LENGTH_SHORT).show();

        // TODO: Open navigation drawer or menu
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

            // TODO: Implement actual search functionality
            // - Filter news cards based on search query
            // - Navigate to search results screen
            // - Connect to backend search API
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

        // TODO: Navigate to specific category screens
        // switch (tabName) {
        //     case "Sport":
        //         Intent sportIntent = new Intent(MainActivity.this, SportActivity.class);
        //         startActivity(sportIntent);
        //         break;
        //     case "Education":
        //         Intent educationIntent = new Intent(MainActivity.this, EducationActivity.class);
        //         startActivity(educationIntent);
        //         break;
        //     case "Event":
        //         Intent eventIntent = new Intent(MainActivity.this, EventActivity.class);
        //         startActivity(intent);
        //         break;
        // }
    }

    /**
     * Method to update greeting text with username
     * Call this method after successful login
     */
    public void setUserGreeting(String username) {
        if (username != null && !username.isEmpty()) {
            greetingText.setText("Hi, " + username);
        }
    }
}