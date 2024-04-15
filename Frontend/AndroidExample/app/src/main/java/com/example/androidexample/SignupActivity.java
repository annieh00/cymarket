package com.example.androidexample;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import com.example.androidexample.Const;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;

/**
 * The signup activity class grants the user to be able to sign up with credentials. It extends AppCompatActivity.
 */
public class SignupActivity extends AppCompatActivity {

    private EditText emailEditText;  // define username edittext variable
    private EditText passwordEditText;  // define password edittext variable
    private EditText confirmEditText;   // define confirm edittext variable
    private Button loginButton;         // define login button variable
    private Button signupButton;        // define signup button variable
    private String TAG = SignupActivity.class.getSimpleName(); //the tag used to identify JSON object requests
    public String email;
    public Boolean signupSuccess = false;
    public String passsword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /* initialize UI elements */
        //Text
        emailEditText = findViewById(R.id.signup_username_edt);  // link to username edtext in the Signup activity XML
        passwordEditText = findViewById(R.id.signup_password_edt);  // link to password edtext in the Signup activity XML
        confirmEditText = findViewById(R.id.signup_confirm_edt);    // link to confirm edtext in the Signup activity XML

        //Buttons
//        loginButton = findViewById(R.id.signup_login_btn);    // link to login button in the Signup activity XML
        signupButton = findViewById(R.id.signup_signup_btn);  // link to signup button in the Signup activity XML


        //asking if the user already has an account
        TextView txtRegister = (TextView)findViewById(R.id.loginTxtBtn);
        txtRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);  // go to SignupActivity
            }
        });
        /* click listener on login button pressed */
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                /* when login button is pressed, use intent to switch to Login Activity */
//                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
//                startActivity(intent);  // go to LoginActivity
//            }
//        });

        /* click listener on signup button pressed */
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* grab strings from user inputs */
                String username = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirm = confirmEditText.getText().toString();
                sendJsonObjReq();

            }
        });
    }

    /**
     * Send data as a post request to the server. This data will be email and password, and it will create a user based
     * off of the given credentials. The response will be a boolean that says if the creation is successful or not.
     */
    private void sendJsonObjReq() {
        JSONObject jsonObject = new JSONObject();
        try {
            //input your API parameters
            jsonObject.put("email", emailEditText.getText().toString());
            jsonObject.put("password", passwordEditText.getText().toString());
            int atInex = emailEditText.getText().toString().lastIndexOf("@");
            jsonObject.put("userName",emailEditText.getText().toString().substring(0,atInex));

            //            Toast.makeText(LoginActivity.this, "got e and p", Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        System.out.println(jsonObject.toString());
//        String URL_POST_LOGIN_USER = "https://07537acc-da80-4457-8b10-ff9e97cbea07.mock.pstmn.io/data3";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_POST_REGISTER_USER, jsonObject, response -> {
            Log.d(TAG, response.toString());
            try {
                signupSuccess = response.getBoolean("fromServer");
//                Toast.makeText(SignupActivity.this, "" + alreadyExists, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            if (signupSuccess){
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(SignupActivity.this, "Please sign in", Toast.LENGTH_LONG).show();
            }
//            else{
//                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
//                Toast.makeText(SignupActivity.this, "User does not exist", Toast.LENGTH_LONG).show();
//                startActivity(intent);
//            }

        }, error -> {
            VolleyLog.d(TAG, "Error: " + error.getMessage());
//            Toast.makeText(SignupActivity.this, "User Not Found", Toast.LENGTH_LONG).show();
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }

        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);


    }
}