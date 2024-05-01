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

import com.android.volley.toolbox.Volley;
import com.example.androidexample.Const;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;

/**
 * The login activity will be a screen where the user can log in if given the right credentials.
 */
public class LoginActivity extends AppCompatActivity {
    private EditText emailTxt, passwordTxt;  //these text boxes are where the user enters their credentials
    private Button loginButton;         // the login button is used to submit the credentials of the user
    private Button signupButton;        // the sign up button is used to indicate that the user wants to sign up, and it will lead to another screen
 //   public static String username;    //the user's full name given to Iowa State
    private String TAG = LoginActivity.class.getSimpleName(); //the tag used to identify JSON object requests
//    public static int userID = 0;       //id of the user who is currently logged in
    public static int permission = 0; //0=admin, 1=organizer, 2=normal user
//    public static String profilePicture;        //the user's profile picture
    public static String password;      //the user's password
    public static String firstName;//

    public static String lastName;
    public static String userType;
    public static String username;

    public static int loginID;
    public static String email;         //the user's email
//    public static String dateCreated; //the date that the account was created
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req"; //this is the tag names
//    private Boolean userValidity = true; //this boolean is meant to validate the user
    private Boolean txtValidity = true;
    public Boolean validUser;
    private EditText firstNameEditTxt;
    private EditText lastNameEditTxt;

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
//        signupButton = findViewById(R.id.login_signup_btn);  // link to signup button in the Login activity XML
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());


        /* click listener on signup button pressed */
//        signupButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                /* when signup button is pressed, use intent to switch to Signup Activity */
//                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
//                startActivity(intent);  // go to SignupActivity
//            }
//        });

        //asking if the user hasn't created an account yet
        TextView txtRegister = (TextView)findViewById(R.id.signupTxtBtn);
        txtRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                  Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                  startActivity(intent);  // go to SignupActivity
            }
        });
        /* click listener on login button pressed */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sendJsonObjReq();
                if (v.getId() == R.id.login_login_btn){
                    Pass();
                }
                /* grab strings from user inputs */
//                if (txtValidity == true) {
                     sendJsonObjReq();
//                    Pass();
//                }else if (!txtValidity){
//                    Pass();
//                    Toast.makeText(LoginActivity.this, "User Not Valid", Toast.LENGTH_LONG).show();
//                }
                /* when login button is pressed, use intent to switch to Login Activity */
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                intent.putExtra("USERNAME", username);  // key-value to pass to the MainActivity
//                intent.putExtra("PASSWORD", password);  // key-value to pass to the MainActivity
//                startActivity(intent);  // go to MainActivity with the key-value data
            }
        });
    }


    public void Pass(){
//        if (!(emailTxt.getText().toString().contains("@iastate.edu"))){
//            txtValidity = false;
//        }else if (passwordTxt.getText().length() < 8){
//            passwordTxt.setError("Password must be at least 8 characters long");
//            txtValidity = false;
//        }else{
//            txtValidity = true;
//        }
    }

    String URL_POST_LOGIN_USER = "https://07537acc-da80-4457-8b10-ff9e97cbea07.mock.pstmn.io/user1";

    /**
     *     Sends a post request to the server to post the login info. If the credentials are already in the database, it will log in
     *     (because a boolean is sent as true). If not, it will send back a boolean that is false which means that the user is not in the system.
     */
    private void sendJsonObjReq() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject jsonObject = new JSONObject();
        JSONObject body = new JSONObject();
        try {
            //input your API parameters
            jsonObject.put("email", emailTxt.getText().toString().trim());
            jsonObject.put("password", passwordTxt.getText().toString().trim());
            jsonObject.put("firstName", emailTxt.getText().toString().trim());
            jsonObject.put("lastName", passwordTxt.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, "http://coms-309-060.class.las.iastate.edu:8080/login", jsonObject, response -> {
            Log.d(TAG, response.toString());
            try {
//                email = response.getString("email");
//                password = response.getString("password");
                validUser = response.getBoolean("fromServer");
                permission = response.getInt("permission");
                //I, jess added these two lines
                username = response.getString("username");
                loginID = response.getInt("id");
                Toast.makeText(LoginActivity.this, "validUser : " + username, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                Toast.makeText(LoginActivity.this, "User Not Found", Toast.LENGTH_LONG).show();
            }

            if (validUser == true && permission == 0){
                Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, MainFeedAdmin.class);
                startActivity(intent);
            }else if (validUser == true && permission == 1) {
                Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, MainFeedOrganizer.class);
                startActivity(intent);
            }else if (validUser == true && permission == 2){
                Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, MainFeed.class);
                startActivity(intent);
            }else{
                Toast.makeText(LoginActivity.this, "User Not Found", Toast.LENGTH_LONG).show();
            }

        }, error -> {
            VolleyLog.d(TAG, "Error: " + error.getMessage());
            Toast.makeText(LoginActivity.this, "L", Toast.LENGTH_LONG).show();
            txtValidity = true;
        }) {

            /**
             * Passing some request headers
             */
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

        //queue.add(jsonObjReq);
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
        //VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().start();


    }
}