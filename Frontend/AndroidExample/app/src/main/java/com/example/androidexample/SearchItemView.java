package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.Post.PostAdapter;
import com.example.androidexample.Post.PostItemObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchItemView extends AppCompatActivity {

    private String URL = "http://coms-309-060.class.las.iastate.edu:8080";

    String searchQuery;


    Toolbar toolbar;

    public PostAdapter mPostAdapter;


    public RecyclerView mRecyclerViewSearch;

    public ArrayList<PostItemObject> mSearchList = new ArrayList<>();


    private ListView listUsers;

    private List<Friend> users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item_view);

        toolbar = findViewById(R.id.vwebtoolbar1);
        listUsers = findViewById(R.id.list_view_users);

        mRecyclerViewSearch = findViewById(R.id.recycler_view_search);
        LinearLayoutManager linearManager = new LinearLayoutManager(this);
        mRecyclerViewSearch.setLayoutManager(linearManager);

        users = new ArrayList<>();

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
                        try {
                            JSONArray resultsArray = response.getJSONArray("results");
                            for (int i = 0; i < resultsArray.length(); i++) {
                                JSONObject resultObject = resultsArray.getJSONObject(i);
                                if (resultObject.has("firstName")) {
                                    // This is user data
                                    String firstName = resultObject.getString("firstName");
                                    Log.d("USER", firstName);
                                    String lastName = resultObject.getString("lastName");
                                    String email = resultObject.getString("email");
                                    int id = resultObject.getInt("id");
                                    // Create a new Friend object and add it to the users list
                                    users.add(new Friend(firstName, lastName, id, email));
//                                    Log.d("users", users.);
                                } else if (resultObject.has("userName")) {
                                    String picture1 = resultObject.getString("picture1");
                                    String picture2 = resultObject.getString("picture2");
                                    String picture3 = resultObject.getString("picture3");
                                    String picture4 = resultObject.getString("picture4");
                                    String picture5 = resultObject.getString("picture5");
                                    String picture6 = resultObject.getString("picture6");
                                    String title = resultObject.getString("title");
                                    int price = resultObject.getInt("price");
                                    boolean auction = resultObject.getBoolean("isAuction");
                                    String description = resultObject.getString("description");
                                    String userName = resultObject.getString("userName");
                                    int id = resultObject.getInt("id");
                                    Log.d("USERPOST", description);
                                    // Create a new PostItemObject and add it to the list
                                    mSearchList.add(new PostItemObject(picture1, picture2, picture3, picture4, picture5, picture6, title, price, auction, description, userName, id));
//                                    Log.d("posts", mSearchList);
                                }
                            }
                            mRecyclerViewSearch.setAdapter(mPostAdapter);

//                            ListFriendRequests adapter = new ListFriendRequests(SearchActivity.this, friendRequestList, SearchActivity.this);


//                            listUsers.setAdapter();

                            // Update your ListView with user data if needed
                            // Notify adapter of data change
                            // Update RecyclerView with posting data
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error here
                        Log.d("SearchRequest", "Search failed");

                    }
                });

        // Add the request to the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }


}
