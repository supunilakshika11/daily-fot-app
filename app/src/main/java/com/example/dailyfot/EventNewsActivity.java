package com.example.dailyfot; // Replace with your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EventNewsActivity extends AppCompatActivity {

    // UI components
    private ImageView backArrow;
    private ImageView userProfileIcon;
    private ImageView notificationIcon;
    private ImageView eventImage;
    private TextView eventTitle;
    private TextView eventTitleOnImage;
    private TextView eventHeadline;
    private TextView eventDescription;
    private TextView eventCredits;
    private ImageView likeButton;
    private TextView likeCount;
    private TextView timeCount;
    private ImageView timeIcon;
    private TextView dateDay;
    private TextView dateMonth;
    private TextView dateYear;

    // Like state tracking
    private boolean isLiked = false;
    private int currentLikeCount = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_news);

        // Initialize UI components
        initializeViews();

        // Set up click listeners
        setupClickListeners();

        // Load event data
        loadEventData();
    }

    /**
     * Connect Java variables to XML elements
     */
    private void initializeViews() {
        backArrow = findViewById(R.id.back_arrow);
        userProfileIcon = findViewById(R.id.user_profile_icon);
        notificationIcon = findViewById(R.id.notification_icon);
        eventImage = findViewById(R.id.event_image);
        eventTitle = findViewById(R.id.event_title);
        eventTitleOnImage = findViewById(R.id.event_title_on_image);
        eventHeadline = findViewById(R.id.event_headline);
        eventDescription = findViewById(R.id.event_description);
        eventCredits = findViewById(R.id.event_credits);
        likeButton = findViewById(R.id.like_button);
        likeCount = findViewById(R.id.like_count);
        timeCount = findViewById(R.id.time_count);
        timeIcon = findViewById(R.id.time_icon);
        dateDay = findViewById(R.id.date_day);
        dateMonth = findViewById(R.id.date_month);
        dateYear = findViewById(R.id.date_year);
    }

    /**
     * Set up click event handlers
     */
    private void setupClickListeners() {
        // Back arrow click
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

        // Like button click
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLikeClick();
            }
        });
    }

    /**
     * Load event data based on intent
     */
    private void loadEventData() {
        // Get data passed from previous activity
        Intent intent = getIntent();
        String title = intent.getStringExtra("event_title");
        String description = intent.getStringExtra("event_description");

        // Load content based on the event title - FIXED ORDER
        if (title != null) {
            if (title.contains("Noor Feast")) {
                loadNoorFeastEventData();
            } else if (title.contains("International Dance Day")) {
                loadDanceEventData();
            } else if (title.contains("Sinhala Event")) {
                loadSinhalaEventData();
            } else {
                loadDefaultEventData(title, description);
            }
        } else {
            loadNoorFeastEventData(); // Default to Noor Feast
        }

        // Set initial like count and time
        likeCount.setText(String.valueOf(currentLikeCount));
        timeCount.setText("21");
    }

    /**
     * Load Noor Feast event data
     */
    private void loadNoorFeastEventData() {
        eventTitle.setText("Noor Feast Event");
        eventTitleOnImage.setText("Title\ngoes\nhere");
        eventHeadline.setText("Noor Feast Event – Blessed Celebration");
        eventDescription.setText("A blessed celebration of tradition and community bringing together flavors, culture, and joy in a magnificent feast. Join us for an evening of traditional cuisine and cultural heritage.");
        eventCredits.setText("Caption by : Cultural Committee\nDesign by : Event Design Team");
        eventImage.setImageResource(R.drawable.one); // Noor Feast image

        // Set date for Noor Feast
        dateDay.setText("15");
        dateMonth.setText("JUN");
        dateYear.setText("2025");
    }

    /**
     * Load International Dance Day event data
     */
    private void loadDanceEventData() {
        eventTitle.setText("International Dance Day 2025");
        eventTitleOnImage.setText("International\nDANCE DAY");
        eventHeadline.setText("International Dance Day – April 29");
        eventDescription.setText("A global celebration of the art of dance and its power to express what words often can't. From grooving to storytelling, dance unites generations and cultures.");
        eventCredits.setText("Caption by : Apeksha Manchanayaka (1st year - Et dept)\nDesign by : Kavishka Deshan");
        eventImage.setImageResource(R.drawable.two); // International Dance Day image

        // Set date for Dance Day
        dateDay.setText("29");
        dateMonth.setText("APR");
        dateYear.setText("2025");
    }

    /**
     * Load Sinhala Event data
     */
    private void loadSinhalaEventData() {
        eventTitle.setText("Sinhala Event 2025");
        eventTitleOnImage.setText("Sinhala\nEvent\n2025");
        eventHeadline.setText("Sinhala Cultural Event 2025");
        eventDescription.setText("A cultural celebration showcasing the rich heritage and traditions of Sri Lankan culture with music, dance, and traditional arts. Experience the beauty of Sinhala culture.");
        eventCredits.setText("Caption by : Cultural Society\nDesign by : Traditional Arts Committee");
        eventImage.setImageResource(R.drawable.three); // Sinhala Event image

        // Set date for Sinhala Event
        dateDay.setText("10");
        dateMonth.setText("JUL");
        dateYear.setText("2025");
    }

    /**
     * Load default event data
     */
    private void loadDefaultEventData(String title, String description) {
        eventTitle.setText(title != null ? title : "Event Details");
        eventTitleOnImage.setText("Event\nDetails");
        eventHeadline.setText((title != null ? title : "Event") + " – Details");
        eventDescription.setText(description != null ? description : "Event details will be updated soon.");
        eventCredits.setText("Caption by : Event Committee\nDesign by : Design Team");
        eventImage.setImageResource(R.drawable.one); // Default to Noor Feast image

        // Set default date
        dateDay.setText("TBD");
        dateMonth.setText("TBD");
        dateYear.setText("2025");
    }

    /**
     * Handle back arrow click - return to previous screen
     */
    private void handleBackClick() {
        finish(); // Close current activity and return to previous screen
    }

    /**
     * Handle user profile icon click
     *
     * WHAT THIS DOES:
     * 1. Creates an Intent to open the UserProfileActivity
     * 2. Passes a default username (you can enhance this later to get actual username)
     * 3. Opens the User Information screen
     */
    private void handleUserProfileClick() {
        // Create Intent to navigate to User Information screen
        Intent intent = new Intent(EventNewsActivity.this, UserProfileActivity.class);

        // Pass username (for now using default, you can enhance this later to get the actual username)
        // You could get the username from SharedPreferences or pass it through intents
        intent.putExtra("username", "User");

        // Open the User Information screen
        startActivity(intent);
    }

    /**
     * Handle notification icon click
     */
    private void handleNotificationClick() {
        Toast.makeText(this, "Notifications screen coming soon!", Toast.LENGTH_SHORT).show();

        // TODO: Navigate to notifications screen
        // Intent intent = new Intent(EventNewsActivity.this, NotificationsActivity.class);
        // startActivity(intent);
    }

    /**
     * Handle like button click - toggle like state
     */
    private void handleLikeClick() {
        if (isLiked) {
            // Unlike
            isLiked = false;
            currentLikeCount--;
            likeButton.setImageResource(R.drawable.ic_heart); // Regular heart
            Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
        } else {
            // Like
            isLiked = true;
            currentLikeCount++;
            likeButton.setImageResource(R.drawable.ic_heart_filled); // Filled heart
            Toast.makeText(this, "Added to favorites!", Toast.LENGTH_SHORT).show();
        }

        // Update like count display
        likeCount.setText(String.valueOf(currentLikeCount));
    }
}