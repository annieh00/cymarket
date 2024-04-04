package com.example.androidexample;

// Import necessary Android classes and libraries
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The ViewAnnouncementAdmin class displays announcements for administrators.
 * It extends AppCompatActivity.
 */
public class ViewAnnouncementAdmin extends AppCompatActivity {

    // Declare UI elements and variables
    private Button btnJsonArrReq;
    private Button deleteBtn;
    private Button updateAnnouncement;
    private EditText id;
    private EditText updatedTitle;
    private EditText newUpdatedBody;
    private int announcementID;
    private Toolbar toolbar;
    private static final String URL_JSON_ARRAY_DEL = "http://coms-309-060.class.las.iastate.edu:8080/announcements/del/";
    private static final String URL_JSON_ARRAY_UPDATE = "http://coms-309-060.class.las.iastate.edu:8080/announcements/update/";
    private static final String URL_JSON_ARRAY = "http://coms-309-060.class.las.iastate.edu:8080/announcements";
    private ListAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_announcement_admin);

        // Initialize UI elements
        btnJsonArrReq = findViewById(R.id.announcementsBtn);
        updateAnnouncement = findViewById(R.id.updateBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        id = findViewById(R.id.idToUpdate);
        updatedTitle = findViewById(R.id.titleToUpdate);
        newUpdatedBody = findViewById(R.id.bodyToUpdate);
        listView = findViewById(R.id.adminListView);
        toolbar = findViewById(R.id.vwebtoolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Initialize adapter with empty list
        adapter = new ListAdapter(this, new ArrayList<>());
        listView.setAdapter(adapter);

        // Set click listeners for buttons
        btnJsonArrReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeJsonArrayReq();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        updateAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeUpdateToAnnouncement();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int announcementId = Integer.parseInt(id.getText().toString());
                String deleteAnnouncementUrl = URL_JSON_ARRAY_DEL + announcementId;

                // Create DELETE request
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

    /**
     * Makes a PUT request to update an announcement.
     */
    private void makeUpdateToAnnouncement() {
        announcementID = Integer.parseInt(id.getText().toString());
        String updatedTitleText = updatedTitle.getText().toString();
        String updatedBodyText = newUpdatedBody.getText().toString();

        String updateAnnouncementUrl = URL_JSON_ARRAY_UPDATE + announcementID;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", updatedTitleText);
            jsonObject.put("description", updatedBodyText);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create PUT request
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

    /**
     * Makes a GET request to fetch announcements as a JSON array.
     */
    private void makeJsonArrayReq () {

        adapter.clear();

        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
                Request.Method.GET,
                URL_JSON_ARRAY,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());

                        // Parse the JSON array and add data to the adapter
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String title = jsonObject.getString("title");
                                String description = jsonObject.getString("description");


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
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











