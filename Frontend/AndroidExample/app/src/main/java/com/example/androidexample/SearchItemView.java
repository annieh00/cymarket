package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchItemView extends AppCompatActivity {

    private String URL = "http://coms-309-060.class.las.iastate.edu:8080";

    String searchQuery;


    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item_view);

        toolbar = findViewById(R.id.vwebtoolbar1);

        // Retrieve the search query from the Intent
        searchQuery = getIntent().getStringExtra("searchQuery");

        // Find the EditText
        EditText searchedItemEditText = findViewById(R.id.searchedItem);

        // Set the search query to the EditText
        searchedItemEditText.setText(searchQuery);

        searchRequest();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                onBackPressed();
                WebSocketManager.getInstance().disconnectWebSocket();
            }
        });



    }


    public void searchRequest() {
        // URL of your backend API
        String url = URL + "/search/" + LoginActivity.loginID + "?query=" + searchQuery;
        //maybe this idk yet
        // url:8080/search/3?query=""
        // Create JSONObject for parameters
        JSONObject jsonBody = new JSONObject();
        try {
            // Add query parameter
            jsonBody.put("query", searchQuery);
            // Add any other parameters if required
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("SearchRequest", response.toString());

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error here
                        Log.d("SearchRequest", "Search successful");

                    }
                });

        // Add the request to the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }
}