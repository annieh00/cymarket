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

public class SignupActivity extends AppCompatActivity {

    private EditText emailEditText;  // define username edittext variable
    private EditText passwordEditText;  // define password edittext variable
    private EditText confirmEditText;   // define confirm edittext variable
    private Button loginButton;         // define login button variable
    private Button signupButton;        // define signup button variable
    private String TAG = SignupActivity.class.getSimpleName(); //the tag used to identify JSON object requests
    public String email;
    public Boolean alreadyExists;
    public String passsword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /* initialize UI elements */
        emailEditText = findViewById(R.id.signup_username_edt);  // link to username edtext in the Signup activity XML
        passwordEditText = findViewById(R.id.signup_password_edt);  // link to password edtext in the Signup activity XML
        confirmEditText = findViewById(R.id.signup_confirm_edt);    // link to confirm edtext in the Signup activity XML
        loginButton = findViewById(R.id.signup_login_btn);    // link to login button in the Signup activity XML
        signupButton = findViewById(R.id.signup_signup_btn);  // link to signup button in the Signup activity XML

        /* click listener on login button pressed */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when login button is pressed, use intent to switch to Login Activity */
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);  // go to LoginActivity
            }
        });

        /* click listener on signup button pressed */
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* grab strings from user inputs */
                String username = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirm = confirmEditText.getText().toString();

                if (password.equals(confirm)){
                    Toast.makeText(getApplicationContext(), "Signing up", Toast.LENGTH_LONG).show();
                    sendJsonObjReq();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Password don't match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendJsonObjReq() {
        JSONObject jsonObject = new JSONObject();
        try {
            //input your API parameters
            jsonObject.put("email", emailEditText.getText().toString());
            jsonObject.put("password", passwordEditText.getText().toString());

            //            Toast.makeText(LoginActivity.this, "got e and p", Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        System.out.println(jsonObject.toString());
//        String URL_POST_LOGIN_USER = "https://07537acc-da80-4457-8b10-ff9e97cbea07.mock.pstmn.io/user1";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_POST_REGISTER_USER, jsonObject, response -> {
            Log.d(TAG, response.toString());
            try {
//                    Toast.makeText(LoginActivity.this, "Successfully got email and password", Toast.LENGTH_LONG).show();
//                    Toast.makeText(LoginActivity.this, "assigning values", Toast.LENGTH_LONG).show();
//                email = response.getString("email");
//                 = response.getString("password");
                alreadyExists = response.getBoolean("fromServer");

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

                if (alreadyExists == true){
                    Toast.makeText(SignupActivity.this, "User Already Exists", Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    Toast.makeText(SignupActivity.this, "Login", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }


//                    Toast.makeText(LoginActivity.this, "User Not Found", Toast.LENGTH_LONG).show();
//                }

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