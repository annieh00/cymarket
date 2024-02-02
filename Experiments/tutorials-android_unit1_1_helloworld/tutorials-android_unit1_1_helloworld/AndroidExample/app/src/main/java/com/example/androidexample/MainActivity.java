package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;   // define message textview variable
    private Button Login;
    private Button SignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // link to Main activity XML
        Login = findViewById(R.id.button3);
        /* initialize UI elements */
        messageText = findViewById(R.id.main_msg_txt);
        SignUp = findViewById(R.id.button2);

        // link to message textview in the Main activity XML
        messageText.setText("Hello Annie Huynh");

        Bundle extras = getIntent().getExtras();
        if (extras == null){
            messageText.setText("Hello Jessica Guerrero");
        }else{

        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(packageContext:MainActivity.this, CounterActivity.class);
            }
        });

    }
}
