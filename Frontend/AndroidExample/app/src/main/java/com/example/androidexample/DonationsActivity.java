package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


/**
 * Donations activity will be a feature for organization users. Items that have been marked as donations
 * will appear in the screen.
 */
public class DonationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations);
    }
}