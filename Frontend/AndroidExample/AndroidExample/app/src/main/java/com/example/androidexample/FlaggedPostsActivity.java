package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * The user will be able to flag posts as suspicious or not, and the admin will be able to see the flagged posts and
 * delete them if necessary.
 */
public class FlaggedPostsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flagged_posts);
    }
}