package com.example.androidexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
//import android.widget.ListAdapter;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import java.util.ArrayList;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
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
import com.example.androidexample.R.menu.*;

/**
 * Main feed displays the current posts.
 */
public class MainFeed extends AppCompatActivity {

    private DrawerLayout nDrawerLayout;


    AlertDialog.Builder builder;
    public static final String URL_IMAGE = "http://10.0.2.2:8080/images/1";

    private ListAdapter adapter;
    private ListView listView;
    private String itemSelected;

    /**
     *
     */
    private ImageView imageView;

    /**
     * This is the adapter.
     */
    private PostAdapter postAdapter;
    /**
     * this is a tag that is attached to the log
     */
    private String TAG = MainFeed.class.getSimpleName();


    /**|
     * this is the recycler view
     */
    private RecyclerView mRecyclerView;




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

//        xCoord = findViewById(R.id.xInput);
//        yCoord = findViewById(R.id.yInput);
//        setLocationBtn = findViewById(R.id.locationButton);
//        seeCoordinates = findViewById(R.id.listCoords);
//        coordListing = findViewById(R.id.coordList);
//        deleteCoordButton = findViewById(R.id.deleteCoord);
//        id = findViewById(R.id.coordIdDelete);
//        updateLocationBtn = findViewById(R.id.updateCoord);
//        updatedX = findViewById(R.id.updateX);
//        updatedY = findViewById(R.id.updateY);



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


//        setLocationBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                final String
////
////                String announcement = adminMessage.toString();
////                sendAnnouncementToServer(announcement);
//
//                final String x, y;
//                x = xCoord.getText().toString();
//                y = yCoord.getText().toString();
//
//
//                JSONObject jsonBody = new JSONObject();
//                try {
//                    jsonBody.put("x", x);
//                    jsonBody.put("y", y);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, server_url_create, jsonBody, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        builder.setTitle("Server Response");
//                        try {
//                            builder.setMessage("Response " + response.getString("status"));
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                xCoord.setText("");
//                                yCoord.setText("");
//                            }
//                        });
//                        AlertDialog alertDialog = builder.create();
//                        alertDialog.show();
//
//                    }
//
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(MainFeed.this, "Error....", Toast.LENGTH_LONG).show();
//                        error.printStackTrace();
//                    }
//                }) {
//                    //                    @Nullable
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<String, String>();
////
////                        params.put("title", msgTitle);
////                        params.put("description", message);
////
//                        return params;
//                    }
//                };
//
//                MySingleton.getInstance(MainFeed.this).addToRequestQueue(jsonObjReq);
//
//            }
//        });
//
//
//
//        seeCoordinates.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                makeJsonArrayReq();
//            }
//        });

        /** If a certain screen is pressed, it will go to that certain screen.
         *
         */
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                itemSelected = item.toString();
                Intent intent;
                switch (itemSelected) {
                    case "Auction":
                        intent = new Intent(getApplicationContext(), AuctionActivity.class);
                        startActivity(intent);
                        break;
                    case "Profile":
                        // Handle click on the first item
                        intent = new Intent(getApplicationContext(), ProfileSetUpActivity.class);
                        startActivity(intent);
                        break;
                    case "Sell":
                        // Handle click on the second item
                        intent = new Intent(getApplicationContext(), CreatePostActivity.class);
                        startActivity(intent);
                        break;
                    case "Inbox":
                        // Handle click on the third item
                        intent = new Intent(getApplicationContext(), InboxActivity.class);
                        startActivity(intent);
                        break;
                    case "Announcements":
                        // Handle click on the fourth item
                        intent = new Intent(getApplicationContext(), ViewAnnouncementAdmin.class);
                        startActivity(intent);
                        break;
                    case "Settings":
                        // Handle click on the fourth item
                        intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intent);
                        break;
                }

                // Close the navigation drawer after handling the click
                nDrawerLayout.closeDrawers();

                return true; // Return true to indicate that the item is selected
            }
        });

//        updateLocationBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int uid = Integer.parseInt(id.getText().toString());
//
//
//                String x = updatedX.getText().toString();
//                String y = updatedY.getText().toString();
//
//                String updateAnnouncementUrl = server_url_update + uid;
//
//                JSONObject jsonObject = new JSONObject();
//                try {
//                    jsonObject.put("x", x);
//                    jsonObject.put("y", y);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, updateAnnouncementUrl, jsonObject, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.d("Volley Response", "Announcement updated successfully");
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("Volley Error", "Error updating announcement: " + error.getMessage());
//                    }
//                });
//                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
//            }
//        });
//
//
//
//        deleteCoordButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int announcementId = Integer.parseInt(id.getText().toString());
////                String deleteAnnouncementUrl = "http://coms-309-060.class.las.iastate.edu:8080/announcements/del/" + announcementId;
//                String deleteAnnouncementUrl = server_url_del + announcementId ;
//
//                StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, deleteAnnouncementUrl,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                Log.d("Volley Response", response);
//                                // Handle successful deletion (if needed)
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Log.e("Volley Error", "Error deleting announcement: " + error.getMessage());
//                                // Handle error response (if needed)
//                            }
//
//                        });
//                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(deleteRequest);
//
//
//            }
//        });


    }

    private void jsonParse(){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Const.URL_GET_ALL_POSTS, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("posts");
                        for (int i = 0; i < jsonArray.length(); i++){

                        }
                    }catch(JSONException e){
                        throw new RuntimeException(e);
                    }
                })

    }
//    private void makeJsonArrayReq () {
//
//        coordListing.setText("");
//        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
//                Request.Method.GET,
//                server_url_list,
//                null, // Pass null as the request body since it's a GET request
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d("Volley Response", response.toString());
//
//
//
//                        StringBuilder stringBuilder = new StringBuilder();
//                        // Parse the JSON array and add data to the adapter
//                        for (int i = 0; i < response.length(); i++) {
//                            try {
//                                JSONObject jsonObject = response.getJSONObject(i);
//                                String x = jsonObject.getString("x");
//                                String y = jsonObject.getString("y");
//
//                                // Create a ListItemObject and add it to the adapter
////                                    ListItemObjectCoords item = new ListItemObjectCoords(x, y);
////                                    adapter.add(item);
//                                stringBuilder.append("X: ").append(x).append(", Y: ").append(y).append("\n");
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        coordListing.setText(stringBuilder.toString());
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("Volley Error", error.toString());
//                    }
//                }) {
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<>();
////                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
////                headers.put("Content-Type", "application/json");
//                return headers;
//            }
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
////                params.put("param1", "value1");
////                params.put("param2", "value2");
//                return params;
//            }
//        };
//
//        // Adding request to request queue
//        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrReq);
//    }
    /**
     * Making image request
     * */
    private void makeImageRequest() {

        ImageRequest imageRequest = new ImageRequest(
                URL_IMAGE,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Display the image in the ImageView
                        imageView.setImageBitmap(response);
                    }
                },
                0, // Width, set to 0 to get the original width
                0, // Height, set to 0 to get the original height
                ImageView.ScaleType.FIT_XY, // ScaleType
                Bitmap.Config.RGB_565, // Bitmap config

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        Log.e("Volley Error", error.toString());
                    }
                }
        );

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(imageRequest);
    }
}


