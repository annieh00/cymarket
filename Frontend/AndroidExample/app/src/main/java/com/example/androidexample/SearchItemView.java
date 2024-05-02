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

    public PostAdapter mSearchPostAdapter;


    public RecyclerView mRecyclerViewSearch;

    public ArrayList<PostItemObject> mSearchList = new ArrayList<>();


    private ListView listUsers;

    private List<Friend> users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item_view);

        toolbar = findViewById(R.id.vwebtoolbar1);
//        listUsers = findViewById(R.id.list_view_users);

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

        mSearchPostAdapter = new PostAdapter(mSearchList, new PostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PostItemObject post) {
                Log.d("Hi"," Bye");
                Intent intent = new Intent(getApplicationContext(), PostDetailActivity.class);
                intent.putExtra("id", String.valueOf(post.getPostID())); // +1 because the online example doesnt have "https://jsonplaceholder.typicode.com/users/0", just for demostration
                startActivity(intent);
            }
        });

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

        // Clear the search list before making a new request
        mSearchList.clear();

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
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");

                            Log.d("results", response.toString());

                            for (int i = jsonArray.length() - 1; i >= 0; i--) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String picture1 = jsonObject.getString("picture1");
                                String picture2 = jsonObject.getString("picture2");
                                String picture3 = jsonObject.getString("picture3");
                                String picture4 = jsonObject.getString("picture4");
                                String picture5 = jsonObject.getString("picture5");
                                String picture6 = jsonObject.getString("picture6");
                                String title = jsonObject.getString("title");
                                int price = jsonObject.getInt("price");
                                Boolean auction = jsonObject.getBoolean("isAuction");
                                String description = jsonObject.getString("description");
                                String userName = jsonObject.getString("userName");
                                int id = jsonObject.getInt("id");
                                String pic1Data = jsonObject.getString("picture1Data");


                                mSearchList.add(new PostItemObject(pic1Data, picture1, picture2, picture3, picture4, picture5, picture6, title, price, auction, description, userName, id));
                            }

                            // Notify any listeners about the data change
                            mSearchPostAdapter.notifyDataSetChanged();
                            mRecyclerViewSearch.setAdapter(mSearchPostAdapter);
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
