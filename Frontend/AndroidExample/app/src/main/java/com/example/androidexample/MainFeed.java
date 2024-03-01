package com.example.androidexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.navigation.NavigationView;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainFeed extends AppCompatActivity {

    private DrawerLayout nDrawerLayout;

    private EditText xCoord;

    private EditText yCoord;

    private Button setLocationBtn;

    AlertDialog.Builder builder;


//    String server_url = "https://37668f7b-a5c8-475c-821b-06324c4610a1.mock.pstmn.io/admin";
String server_url = "http://coms-309-060.class.las.iastate.edu:8080/meetinglocation/create";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);

        xCoord = findViewById(R.id.xInput);
        yCoord = findViewById(R.id.yInput);
        setLocationBtn = findViewById(R.id.locationButton);


        builder = new AlertDialog.Builder(MainFeed.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        NavigationView navigationView = findViewById(R.id.nav_view);
        nDrawerLayout = findViewById(R.id.drawer);
        navigationView.setItemIconTintList(null);

        ActionBar supportActionBar = getSupportActionBar();
        if(supportActionBar != null){

            VectorDrawableCompat indicator = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(),R.color.darkGrey,getTheme()));

            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);

        }

        setLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                final String
//
//                String announcement = adminMessage.toString();
//                sendAnnouncementToServer(announcement);

                final String x, y;
                x = xCoord.getText().toString();
                y = yCoord.getText().toString();


                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("x", x);
                    jsonBody.put("y", y);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, server_url, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        builder.setTitle("Server Response");
                        try {
                            builder.setMessage("Response " + response.getString("status"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                xCoord.setText("");
                                yCoord.setText("");
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainFeed.this, "Error....", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }){
                    //                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
//
//                        params.put("title", msgTitle);
//                        params.put("description", message);
//
                        return params;
                    }
                };

                MySingleton.getInstance(MainFeed.this).addToRequestQueue(jsonObjReq);

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
              Intent intent = new Intent(getApplicationContext(), ViewPosts.class);
              intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//              intent.putExtra("URL", website); IDK ABOUT THIS LINE
              getApplicationContext().startActivity(intent);


              nDrawerLayout.closeDrawers();

                return false;
            }
        });





    }

}