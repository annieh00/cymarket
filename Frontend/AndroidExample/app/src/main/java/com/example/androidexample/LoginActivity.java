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

import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText emailTxt, passwordTxt;  //these text boxes are where the user enters their credentials
    private Button loginButton;         // the login button is used to submit the credentials of the user
    private Button signupButton;        // the sign up button is used to indicate that the user wants to sign up, and it will lead to another screen
    public static String username;    //the user's full name given to Iowa State
    private String TAG = LoginActivity.class.getSimpleName(); //the tag used to identify JSON object requests
    public static int userID = 0;       //id of the user who is currently logged in
    public static int myPermission = 0; //0=admin, 1=organizer, 2=normal user
    public static String profilePicture;        //the user's profile picture
    public static String password;      //the user's password
    public static String email;         //the user's email
    public static String dateCreated;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req"; //this is the tag names
    private Boolean userValidity = true; //this boolean is meant to validate the user


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);            // link to Login activity XML

        /* initialize UI elements */
        //Text
        emailTxt = findViewById(R.id.login_username_edt);
        passwordTxt = findViewById(R.id.login_password_edt);

        //Buttons
        loginButton = findViewById(R.id.login_login_btn);    // link to login button in the Login activity XML
        signupButton = findViewById(R.id.login_signup_btn);  // link to signup button in the Login activity XML


        /* click listener on signup button pressed */
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when signup button is pressed, use intent to switch to Signup Activity */
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);  // go to SignupActivity
            }
        });

        /* click listener on login button pressed */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* grab strings from user inputs */
                if (userValidity == true) {
                    sendJsonObjReq();
                }

                /* when login button is pressed, use intent to switch to Login Activity */
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                intent.putExtra("USERNAME", username);  // key-value to pass to the MainActivity
//                intent.putExtra("PASSWORD", password);  // key-value to pass to the MainActivity
//                startActivity(intent);  // go to MainActivity with the key-value data
            }
        });
    }






    private void sendJsonObjReq() {
        JSONObject jsonObject = new JSONObject();
        try {
            //input your API parameters
            jsonObject.put("email", emailTxt.getText().toString());
            jsonObject.put("password", passwordTxt.getText().toString());
//            Toast.makeText(LoginActivity.this, "got e and p", Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(jsonObject.toString());
        String URL_POST_LOGIN_USER = "https://07537acc-da80-4457-8b10-ff9e97cbea07.mock.pstmn.io/user1";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                URL_POST_LOGIN_USER, jsonObject, response -> {
            Log.d(TAG, response.toString());
            try {
                Toast.makeText(LoginActivity.this, "assigning values", Toast.LENGTH_LONG).show();
                email = response.getString("email");
                password = response.getString("password");
                try {
                    username = response.getString("username");
                    profilePicture = response.getString("pfp");
                    userID = response.getInt("userId");
                    dateCreated = response.getString("dateCreated");
                } catch (JSONException e) {
                    profilePicture = "";
                }
                try {
                    myPermission = response.getInt("permission");
                } catch (Exception e) {
                    myPermission = 2;
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);

        }, error -> {
            VolleyLog.d(TAG, "Error: " + error.getMessage());
            Toast.makeText(LoginActivity.this, "User Not Found", Toast.LENGTH_LONG).show();
            userValidity = false;
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

        };

    }
}