package com.example.androidexample;

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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewAnnouncementAdmin extends AppCompatActivity {


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



    //    private static final String URL_JSON_ARRAY = "https://jsonplaceholder.typicode.com/users";
//    private static final String URL_JSON_ARRAY = "https://37668f7b-a5c8-475c-821b-06324c4610a1.mock.pstmn.io/announcements";
    private static final String URL_JSON_ARRAY = "http://coms-309-060.class.las.iastate.edu:8080/announcements";
    private ListAdapter adapter;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_announcement_admin);


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


        // Initialize the adapter with an empty list (data will be added later)
        adapter = new ListAdapter(this, new ArrayList<>());
        listView.setAdapter(adapter);

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
//                String deleteAnnouncementUrl = "http://coms-309-060.class.las.iastate.edu:8080/announcements/del/" + announcementId;
                String deleteAnnouncementUrl = URL_JSON_ARRAY_DEL + announcementId ;

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


        private void makeUpdateToAnnouncement () {
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

                                    // Create a ListItemObject and add it to the adapter
                                    ListItemObject item = new ListItemObject(title, description);
                                    adapter.add(item);

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











