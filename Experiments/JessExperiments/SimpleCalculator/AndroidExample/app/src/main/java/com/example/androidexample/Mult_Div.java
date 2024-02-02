package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Mult_Div extends AppCompatActivity {
    private EditText num1;
    private EditText num2;
    private Button multiply;
    private TextView result;

    private Button divide;

    private Button goback;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mult_div);

        num1 = (EditText)findViewById(R.id.etNum1);
        num2 = (EditText)findViewById(R.id.etNum2);
        multiply = (Button)findViewById(R.id.btnMult);
        result = (TextView)findViewById(R.id.tvAnswer);
        divide = (Button)findViewById(R.id.btnDiv);

        goback = (Button)findViewById(R.id.btnRtrn);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Mult_Div.this, Add_sub.class);
                startActivity(intent);
            }
        });

        multiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number1 = Integer.parseInt(num1.getText().toString());
                int number2 = Integer.parseInt(num2.getText().toString());
                int sum = number1 * number2;

                result.setText("Answer:  " + String.valueOf(sum));
            }
        });

        divide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number1 = Integer.parseInt(num1.getText().toString());
                int number2 = Integer.parseInt(num2.getText().toString());
                int sum = number1 / number2;

                result.setText("Answer:  " + String.valueOf(sum));
            }
        });
    }
}