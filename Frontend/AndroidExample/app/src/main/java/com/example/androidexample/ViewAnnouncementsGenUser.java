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
public class ViewAnnouncementsGenUser extends AppCompatActivity {

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
        setContentView(R.layout.activity_view_view_announcements_genuser);

        toolbar = findViewById(R.id.vwebtoolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        announcements = findViewById(R.id.AnnouncementsListView);

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





}


