package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.Fragment.RecentSearchesFragment;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    SearchActivityTabAdapter searchActivityTabAdapter;
    ImageButton sendSearch;
    EditText searchInquiry;
    TextView clearHistory;

    private String URL = "http://coms-309-060.class.las.iastate.edu:8080";

//    private ArrayAdapter<String> searchResultAdapter;
//    private List<String> searchResults = new ArrayList<>();


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
//// Initialize the ArrayAdapter for search results
//        searchResultAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searchResults);
//
//        // Other initialization code...






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
                // Check if the search inquiry is not empty
                String searchQuery = searchInquiry.getText().toString();
                if (!searchQuery.isEmpty()) {
                    // Start the new activity here
                    Intent intent = new Intent(SearchActivity.this, SearchItemView.class);
                    // Pass any data to the new activity if needed
                    intent.putExtra("searchQuery", searchQuery);
                    startActivity(intent);
                } else {
                    // Handle case where search inquiry is empty
                    Log.d("SearchActivity", "Please enter a search query");
                }
            }
        });
        pullRecentSearches();

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

                        List<String> lastThreeSearches = new ArrayList<>();
                        int length = response.length();

                        for (int i = length - 1; i >= Math.max(0, length - 3); i--) {
                            try {
                                // Get the search item at index i
                                String searchItem = response.getString(i);
                                // Add the search item to the list
                                lastThreeSearches.add(searchItem);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        int currentItem = viewPager2.getCurrentItem();
                        SearchActivityTabAdapter adapter = (SearchActivityTabAdapter) viewPager2.getAdapter();
                        Fragment currentFragment = adapter.getFragmentAtPosition(currentItem);
                        if (currentFragment instanceof RecentSearchesFragment) {
                            ((RecentSearchesFragment) currentFragment).updateRecentSearches(lastThreeSearches);
                        }

                        Log.d("Last 3 Searches", lastThreeSearches.toString());




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