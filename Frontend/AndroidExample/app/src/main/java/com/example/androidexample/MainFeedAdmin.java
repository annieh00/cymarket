package com.example.androidexample;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainFeedAdmin extends AppCompatActivity {

    private DrawerLayout nDrawerLayout;

    private EditText adminMessage;

    private Button sendMsgBtn;

    String server_url = "http://coms-309-060.class.las.iastate.edu:8080/announcements/create";
    //insert url here


    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_feed_admin);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed_admin);

        adminMessage = findViewById(R.id.announcementText);    // link to confirm edtext in the Signup activity XML
        sendMsgBtn = findViewById(R.id.sendMsgBtn);
        builder = new AlertDialog.Builder(MainFeedAdmin.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        NavigationView navigationView = findViewById(R.id.nav_view);
        nDrawerLayout = findViewById(R.id.drawer);
        navigationView.setItemIconTintList(null);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {

            VectorDrawableCompat indicator = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(), R.color.darkGrey, getTheme()));

            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);

        }


        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                final String
//
//                String announcement = adminMessage.toString();
//                sendAnnouncementToServer(announcement);

                final String message;
                message = adminMessage.getText().toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                builder.setTitle("Server Response");
                                builder.setMessage("Response" + response);
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        adminMessage.setText("");
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainFeedAdmin.this, "Error....", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }){
//                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("message", message);
                        return params;
                    }
                };

                MySingleton.getInstance(MainFeedAdmin.this).addToRequestQueue(stringRequest);

            }
        });
    }
}









