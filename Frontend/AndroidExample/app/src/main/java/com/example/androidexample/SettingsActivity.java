package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {

    Button editProfileButton;
    private SwitchCompat nightModeSwitch;
    private SwitchCompat notificationsSwitch;
    private SwitchCompat privateAccountSwitch;
    ImageButton securityAndPrivacyButton;
    ImageButton textSizeButton;
    ImageButton languagesButton;
    ImageButton sendUsAMessageButton;
    ImageButton aboutUsButton;
    ImageButton FAQsButton;
    ImageButton logOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /* Initialize UI elements  */
        editProfileButton = findViewById(R.id.editProfileButton);
        nightModeSwitch = findViewById(R.id.nightModeSwitch);
        notificationsSwitch = findViewById(R.id.notificationsSwitch);
        privateAccountSwitch = findViewById(R.id.privateAccouuntSwitch);
        securityAndPrivacyButton = findViewById(R.id.securityAndPrivacyArrow);
        textSizeButton = findViewById(R.id.textSizeArrow);
        languagesButton = findViewById(R.id.languagesArrow);
        sendUsAMessageButton = findViewById(R.id.sendUsAMessageArrow);
        aboutUsButton = findViewById(R.id.aboutUsArrow);
        FAQsButton = findViewById(R.id.FAQArrow);
        logOutButton = findViewById(R.id.logOutArrow);

        /* click listener on login button pressed */
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when login button is pressed, use intent to switch to Login Activity */
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(intent);  // go to LoginActivity
            }
        });
    }
}
