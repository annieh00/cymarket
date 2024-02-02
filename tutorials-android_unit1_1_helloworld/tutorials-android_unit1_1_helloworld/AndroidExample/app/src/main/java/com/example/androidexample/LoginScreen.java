package com.example.androidexample;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginScreen extends AppCompatActivity{

        private TextView messageText;   // define message textview variable

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);             // link to Main activity XML

            /* initialize UI elements */
            messageText = findViewById(R.id.main_msg_txt);      // link to message textview in the Main activity XML
            messageText.setText("Hello Annie Huynh");

        }
}

