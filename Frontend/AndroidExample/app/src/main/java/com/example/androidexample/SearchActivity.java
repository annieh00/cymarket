package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;

public class SearchActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;

    SearchActivityTabAdapter searchActivityTabAdapter;


    private String URL = "http://coms-309-060.class.las.iastate.edu:8080";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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


        pullRecentSearches();

    }

    private void pullRecentSearches() {

        //this method needs work, im not sure for the endpoint yet or the structure of the announcements

        String friends_url = URL + "/friends/" + LoginActivity.loginID + "/list";
//        String friends_url = URL + "/friendrequests/" + LoginActivity.username + "/";
//        String friends_url = "https://37668f7b-a5c8-475c-821b-06324c4610a1.mock.pstmn.io" + "/friends";
        //replace userName with LoginActivity.username
//        String friends_url = "https://37668f7b-a5c8-475c-821b-06324c4610a1.mock.pstmn.io" +"/friends/userName123";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, friends_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Toast.makeText(SearchActivity.this, "Error fetching recent searches", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonArrayRequest);








    }


}