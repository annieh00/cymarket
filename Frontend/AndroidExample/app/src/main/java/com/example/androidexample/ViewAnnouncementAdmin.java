package com.example.androidexample;

// Import necessary Android classes and libraries
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
//    private static final String URL_JSON_ARRAY = "https://37668f7b-a5c8-475c-821b-06324c4610a1.mock.pstmn.io/announcements";

    //THIS IS THE MAPPING TO LIST ALL ANNOUNCEMENTS THAT WERE MADE
    private static final String URL_JSON_ARRAY = "http://coms-309-060.class.las.iastate.edu:8080";
//    private static final String URL_JSON_ARRAY = "https://37668f7b-a5c8-475c-821b-06324c4610a1.mock.pstmn.io/announcements";

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

        //fetches all announcements
        getAnnouncements();

        createAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet();


            }
        });


    }

    private void showBottomSheet() {

        // Create a BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

        // Inflate the layout for the bottom sheet
        View bottomSheetView = getLayoutInflater().inflate(R.layout.modal_bottom_sheet_announcements, null);

        // Find views in the bottom sheet layout
        EditText editTextTitle = bottomSheetView.findViewById(R.id.announcement_title);
        EditText editTextDescription = bottomSheetView.findViewById(R.id.announcement_body);
        Button buttonSubmit = bottomSheetView.findViewById(R.id.postButton);

        // Set click listener for the submit button
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform actions when the submit button is clicked
                String title = editTextTitle.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();

                // Validate input
                if (!title.isEmpty() && !description.isEmpty()) {
                    // Here you can perform actions such as sending the announcement to the server
                    // You may also update the UI to reflect the newly added announcement
                    postAnnouncement(title, description);

                    // For demonstration purposes, let's log the input
                    Log.d("BottomSheet", "Title: " + title + ", Description: " + description);

                    // Dismiss the bottom sheet dialog
                    bottomSheetDialog.dismiss();
                } else {
                    // Show a message indicating that both fields are required
                    Toast.makeText(ViewAnnouncementAdmin.this, "Both title and description are required", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set the bottom sheet view
        bottomSheetDialog.setContentView(bottomSheetView);

        // Show the bottom sheet dialog
        bottomSheetDialog.show();
    }

    private void postAnnouncement(String title, String description) {
        // Define your endpoint URL
        String postUrl = URL_JSON_ARRAY + "/announcements/create";

//        String postUrl = "https://37668f7b-a5c8-475c-821b-06324c4610a1.mock.pstmn.io/announcements/create";

        // Create a StringRequest for the POST request
        // Create a JSONObject to hold the announcement data
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("title", title);
            requestBody.put("description", description);

        } catch (JSONException e) {
            e.printStackTrace();
            return; // Exit method if JSON creation fails
        }

        // Create a JsonObjectRequest for the POST request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                postUrl,
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        // i want to parse the response so that i am able to save the created announcement id
                        // Handle response from the server
                        Log.d("POST Request", "Response: " + response);

                        Log.d("POST Request", "Title: " + title + ", Posted Data: " + requestBody.toString());

                        int newAnnoncementId = 0;

                        try {
                            newAnnoncementId = response.getInt("id");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        Announcement newAnnouncement = new Announcement(title, description, newAnnoncementId);

                        allAnnouncements.add(newAnnouncement);

                        // Notify the adapter that the data set has changed
                        adapter.notifyDataSetChanged();
                        // Optionally, you can update the UI or perform any additional actions here

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Log.e("POST Request", "Error: " + error.getMessage());
                        // You can display an error message or take other actions as needed
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }


    private void getAnnouncements() {

        String getURL = URL_JSON_ARRAY + "/announcements";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                getURL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Announcement> fetchedAnnouncements = parseAnnouncements(response);
                        allAnnouncements.clear();
                        allAnnouncements.addAll(fetchedAnnouncements);
                        adapter.notifyDataSetChanged();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                }
        );

        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    // Method to parse JSON response and create Announcement objects
    private List<Announcement> parseAnnouncements(JSONArray jsonArray) {
        List<Announcement> announcements = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                int id = jsonObject.getInt("id");
                String title = jsonObject.getString("title");
                String description = jsonObject.getString("description");
                int id = jsonObject.getInt("id");

                // Create Announcement object
                Announcement announcement = new Announcement(title, description, id);
                announcements.add(announcement);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return announcements;
    }



        public void showModalBottomSheet(Announcement announcement) {
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    ViewAnnouncementAdmin.this, com.google.android.material.R.style.Base_Theme_Material3_Light_BottomSheetDialog);
            View bottomSheetView = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.modal_bottom_sheet_update_announcement, null);

            String title = announcement.getTitle();
            String body = announcement.getDescription();

            Log.d("testing update announcement ", title + body);

            // Find the EditText views within the bottom sheet view
            EditText announcementTitle = bottomSheetView.findViewById(R.id.announcement_title);
            EditText announcementBody = bottomSheetView.findViewById(R.id.announcement_body);

            // Set the text for the EditText views
            announcementTitle.setText(title);
            announcementBody.setText(body);

            // Find the button within the bottom sheet view
            Button sendEdit = bottomSheetView.findViewById(R.id.sendEdit);
            Button delete = bottomSheetView.findViewById(R.id.sendDelete);

            sendEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String updatedTitle = announcementTitle.getText().toString().trim();
                    String updatedBody = announcementBody.getText().toString().trim();



                    sendUpdatedAnnouncement(announcement.getID(), updatedTitle, updatedBody);
                    bottomSheetDialog.dismiss();
                }
            });
            
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteAnnouncement(announcement.getID());
                    bottomSheetDialog.dismiss();
                }
            });



            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        }

    private void deleteAnnouncement(int id) {
        // Define the URL for deleting the announcement
        String url = URL_JSON_ARRAY + "/announcements/del/" + id;

        // Create a StringRequest for the DELETE request
        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle successful response
                        // Log the response from the server
                        Log.d("DELETE Request", "Response: " + response);


                        // Call getAnnouncements() to refresh the list
                        getAnnouncements();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        // Log the error message
                        Log.e("DELETE Request", "Error: " + error.getMessage());
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void sendUpdatedAnnouncement(int id, String newTitle, String newBody) {
        String updateAnnouncementURL = URL_JSON_ARRAY + "/announcements/update/" + id;

//        String updateAnnouncementURL = "https://37668f7b-a5c8-475c-821b-06324c4610a1.mock.pstmn.io" + "/announcements/update/" + id;

        JSONObject requestBody = new JSONObject();
        try {

            requestBody.put("title", newTitle);
            requestBody.put("description", newBody);
        } catch (JSONException e) {
            e.printStackTrace();
            return; // Exit method if JSON creation fails
        }

        // Create a JsonObjectRequest for the PUT request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                updateAnnouncementURL,
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("AnnouncementUpdate", "Announcement " + id + " updated successfully");
                        getAnnouncements();
                        adapter.notifyDataSetChanged();



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("AnnouncementUpdate", "Announcement did not update successfully");

                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

}


