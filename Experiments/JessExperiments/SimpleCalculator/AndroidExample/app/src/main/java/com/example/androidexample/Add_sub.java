package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Add_sub extends AppCompatActivity {

//    private TextView messageText;   // define message textview variable

    //variables for widgets
    private EditText num1;
    private EditText num2;
    private Button add;
    private TextView result;

    private Button sub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_sub);             // link to Main activity XML

        //reference it by xml layout
        //function findbyviewid allows you to do that
        num1 = (EditText)findViewById(R.id.etNum1);
        num2 = (EditText)findViewById(R.id.etNum2);
        add = (Button)findViewById(R.id.btnAdd);
        result = (TextView)findViewById(R.id.tvAnswer);
        sub = (Button)findViewById(R.id.btnSub);

        Button multDiv = findViewById(R.id.btnMult_Div);

        multDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_sub.this, Mult_Div.class);
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number1 = Integer.parseInt(num1.getText().toString());
                int number2 = Integer.parseInt(num2.getText().toString());
                int sum = number1 + number2;

                result.setText("Answer:  " + String.valueOf(sum));
            }
        });

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number1 = Integer.parseInt(num1.getText().toString());
                int number2 = Integer.parseInt(num2.getText().toString());
                int sum = number1 - number2;

                result.setText("Answer:  " + String.valueOf(sum));
            }
        });
    }
}