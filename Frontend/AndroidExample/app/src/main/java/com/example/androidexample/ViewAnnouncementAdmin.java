package com.example.androidexample;

// Import necessary Android classes and libraries
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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
    //    private static final String URL_JSON_ARRAY_DEL = "http://coms-309-060.class.las.iastate.edu:8080/announcements/del/";
//    private static final String URL_JSON_ARRAY_UPDATE = "http://coms-309-060.class.las.iastate.edu:8080/announcements/update/";
//    private static final String URL_JSON_ARRAY = "http://coms-309-060.class.las.iastate.edu:8080/announcements";
    private static final String URL_JSON_ARRAY = "https://37668f7b-a5c8-475c-821b-06324c4610a1.mock.pstmn.io/announcements";

    private AnnouncementAdapter adapter;
    private ListView announcements;

    private List<Announcement> allAnnouncements;

    private ImageButton createAnnouncement;

//    private List<Announcement> announcements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_announcement_admin);

        toolbar = findViewById(R.id.vwebtoolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        announcements = findViewById(R.id.AnnouncementsListView);
        createAnnouncement = findViewById(R.id.createAnnouncement);
        allAnnouncements = new ArrayList<>();

        // Initialize the adapter
        adapter = new AnnouncementAdapter(this, allAnnouncements);
        announcements.setAdapter(adapter); // Set the adapter to the ListView

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

//        getAnnouncements();

        createAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet();


            }
        });


    }

    private void showBottomSheet() {
        View view = getLayoutInflater().inflate(R.layout.modal_bottom_sheet_announcements, null);

        // Find buttons by their IDs
        Button post = view.findViewById(R.id.postButton);


        EditText announcementTitleEditText = view.findViewById(R.id.announcement_title);
        EditText announcementBodyEditText = view.findViewById(R.id.announcement_body);


        // Set onClickListener for the "Yes" button
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        // Set onClickListener for the "No" button


        // Create the BottomSheetDialog
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);

        // Show the dialog
        dialog.show();

    }

//    private void getAnnouncements() {
////        adapter.clear();
//
////        String URL_JSON_ARRAY = "http://your-api-url/announcements";
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
//                Request.Method.GET,
//                URL_JSON_ARRAY,
//                null, // Pass null as the request body since it's a GET request
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d("Volley Response", response.toString());
//
//                        // Parse the JSON array and add data to the adapter
//                        for (int i = 0; i < response.length(); i++) {
//                            try {
//                                JSONObject jsonObject = response.getJSONObject(i);
//                                String title = jsonObject.getString("title");
//                                String description = jsonObject.getString("description");
//
//
//                                    Announcement announcement = new Announcement(title, description);
//                                    allAnnouncements.add(announcement);
//
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                        adapter.notifyDataSetChanged();
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
//            // Adding request to request queue
//            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrReq);
//        }
//
//
//}
//
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











