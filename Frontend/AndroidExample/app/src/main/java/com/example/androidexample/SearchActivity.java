package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.Fragment.RecentSearchesFragment;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    SearchActivityTabAdapter searchActivityTabAdapter;
    ImageButton sendSearch;
    EditText searchInquiry;
    TextView clearHistory;

    private String URL = "http://coms-309-060.class.las.iastate.edu:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        sendSearch = findViewById(R.id.searchBtn);
        searchInquiry = findViewById(R.id.searchItem);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.view_pager);
        searchActivityTabAdapter = new SearchActivityTabAdapter(this);
        viewPager2.setAdapter(searchActivityTabAdapter);




        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        sendSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //making sure that search inquiry actually has something
                if(searchInquiry.toString() != null){

                    searchRequest();


                }
            }
        });
        pullRecentSearches();

    }

    private void searchRequest() {
        // URL of your backend API
        String url = URL + "/search/" + LoginActivity.loginID + "?query=" + searchInquiry.getText().toString();
        //maybe this idk yet
        // url:8080/search/3?query=""
        // Create JSONObject for parameters
        JSONObject jsonBody = new JSONObject();
        try {
            // Add query parameter
            jsonBody.put("query", searchInquiry.getText().toString());
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
    private void pullRecentSearches() {

        //this method needs work, im not sure for the endpoint yet or the structure of the announcements

        String friends_url = URL + "/search/" + LoginActivity.loginID + "/history";
//        String friends_url = URL + "/friendrequests/" + LoginActivity.username + "/";
//        String friends_url = "https://37668f7b-a5c8-475c-821b-06324c4610a1.mock.pstmn.io" + "/friends";
        //replace userName with LoginActivity.username
//        String friends_url = "https://37668f7b-a5c8-475c-821b-06324c4610a1.mock.pstmn.io" +"/friends/userName123";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, friends_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("Previous Searches", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Log.d("Error getting previous searches", error.toString());
                    }
                });

        queue.add(jsonArrayRequest);

    }

}