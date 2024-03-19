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
//import android.widget.ListAdapter;
import android.view.MenuItem;
import android.widget.ListView;
import java.util.ArrayList;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.navigation.NavigationView;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.example.androidexample.ListAdapter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainFeed extends AppCompatActivity {

    private DrawerLayout nDrawerLayout;

    private EditText xCoord;

    private EditText yCoord;

    private TextView coordListing;
    private Button setLocationBtn;

    private Button seeCoordinates;

    private Button deleteCoordButton;

    private Button updateLocationBtn;

    AlertDialog.Builder builder;

    private ListAdapter adapter;
    private ListView listView;


    private int idToDelete;
    private EditText id;

    private EditText updatedX;

    private EditText updatedY;



    //    String server_url = "https://37668f7b-a5c8-475c-821b-06324c4610a1.mock.pstmn.io/admin";
//    String server_url = "http://coms-309-060.class.las.iastate.edu:8080/meetinglocation/create";
//    String server_url_list = "http://coms-309-060.class.las.iastate.edu:8080/announcements";

    String server_url_list = "http://coms-309-060.class.las.iastate.edu:8080/meetinglocation";

    String server_url_create = "http://coms-309-060.class.las.iastate.edu:8080/meetinglocation/create";



    String server_url_del = "http://coms-309-060.class.las.iastate.edu:8080/meetinglocation/del/";

    String server_url_update = "http://coms-309-060.class.las.iastate.edu:8080/meetinglocation/update/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);

        xCoord = findViewById(R.id.xInput);
        yCoord = findViewById(R.id.yInput);
        setLocationBtn = findViewById(R.id.locationButton);
        seeCoordinates = findViewById(R.id.listCoords);
        coordListing = findViewById(R.id.coordList);
        deleteCoordButton = findViewById(R.id.deleteCoord);
        id = findViewById(R.id.coordIdDelete);
        updateLocationBtn = findViewById(R.id.updateCoord);
        updatedX = findViewById(R.id.updateX);
        updatedY = findViewById(R.id.updateY);



        builder = new AlertDialog.Builder(MainFeed.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//        adapter = new ListAdapter(this, new ArrayList<>());
//        listView.setAdapter(adapter);


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

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, server_url_create, jsonBody, new Response.Listener<JSONObject>() {
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
                }) {
                    //                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
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



        seeCoordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeJsonArrayReq();
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

        updateLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int uid = Integer.parseInt(id.getText().toString());


                String x = updatedX.getText().toString();
                String y = updatedY.getText().toString();

                String updateAnnouncementUrl = server_url_update + uid;

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("x", x);
                    jsonObject.put("y", y);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, updateAnnouncementUrl, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", "Announcement updated successfully");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error updating announcement: " + error.getMessage());
                    }
                });
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
            }
        });



        deleteCoordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int announcementId = Integer.parseInt(id.getText().toString());
//                String deleteAnnouncementUrl = "http://coms-309-060.class.las.iastate.edu:8080/announcements/del/" + announcementId;
                String deleteAnnouncementUrl = server_url_del + announcementId ;

                StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, deleteAnnouncementUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Volley Response", response);
                                // Handle successful deletion (if needed)
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Volley Error", "Error deleting announcement: " + error.getMessage());
                                // Handle error response (if needed)
                            }

                        });
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(deleteRequest);


            }
        });


    }

    private void makeJsonArrayReq () {

        coordListing.setText("");
        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
                Request.Method.GET,
                server_url_list,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());



                        StringBuilder stringBuilder = new StringBuilder();
                        // Parse the JSON array and add data to the adapter
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String x = jsonObject.getString("x");
                                String y = jsonObject.getString("y");

                                // Create a ListItemObject and add it to the adapter
//                                    ListItemObjectCoords item = new ListItemObjectCoords(x, y);
//                                    adapter.add(item);
                                stringBuilder.append("X: ").append(x).append(", Y: ").append(y).append("\n");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        coordListing.setText(stringBuilder.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrReq);
    }
}
