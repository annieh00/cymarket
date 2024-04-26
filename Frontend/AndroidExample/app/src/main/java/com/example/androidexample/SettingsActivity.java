package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.androidexample.Post.PostAdapter;
import com.example.androidexample.Post.PostItemObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Settings activity will allow you to change up the settings of the user profile
 */
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
//        editProfileButton = findViewById(R.id.editProfileButton);
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

                /* when login button is pressed, use intent to switch to Profile SetUp Activity */
//                Intent intent = new Intent(SettingsActivity.this, .class);
//                startActivity(intent);  // go to LoginActivity
            }
        });

        FAQsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestOrganizer();
            }
        });

    }

    private void requestOrganizer() {
        JSONObject jsonObject = new JSONObject();
        try {

            //input your API parameters
            jsonObject.put("userName", LoginActivity.username);
            Log.d("JSON OBJ:", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,
                Const.URL_REQ_ORGANIZER,
                jsonObject, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
//                        try {
//
//                        }catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
    }
}
