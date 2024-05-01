package com.example.androidexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
//import android.widget.ListAdapter;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.androidexample.Post.PostAdapter;
import com.example.androidexample.Post.PostItemObject;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import org.json.JSONArray;

import java.util.List;


/**
 * Main feed displays the current posts.
 */
public class MainFeed extends AppCompatActivity {

    private DrawerLayout nDrawerLayout;


    AlertDialog.Builder builder;
    public static final String URL_IMAGE = "http://10.0.2.2:8080/images/1";

    private ListAdapter adapter;
    private ListView listView;
    private String itemSelected;

    /**
     *
     */
    private ImageView imageView;

    private List<String> dataList = new ArrayList<>();
    /**
     * this is a tag that is attached to the log
     */
    private String TAG = MainFeed.class.getSimpleName();


    /**
     * the following variables hole the keys for the items to pass to vote poll activity
     */
    public static final Uri EXTRA_postPicture1 = null;
    public static final Uri EXTRA_postPicture2 = null;
    public static final Uri EXTRA_postPicture3 = null;
    public static final Uri EXTRA_postPicture4 = null;
    public static final Uri EXTRA_postPicture5 = null;
    public static final Uri EXTRA_postPicture6 = null;

    public static final String EXTRA_postTitle = "postTitle";
    public static final int EXTRA_postPrice = 0;
    public static final String EXTRA_postDate = "date";
    public static final String EXTRA_postCategory = "category";
    public static final int EXTRA_postFlagCount = 0;
    public static final Boolean EXTRA_postAuction = false;

    public static final int EXTRA_userID = 0;

    public static final int EXTRA_postID = 0;

    public boolean alreadyConnected = false;


    /**
     * recyclerview related variables
     */
    public static PostAdapter mPostAdapter;
    public RecyclerView mRecyclerView;
    public static ArrayList<PostItemObject> mPostList = new ArrayList<>();


    /**
     * this is the tag itself
     */
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";

    private ImageButton refreshBtn;


    //    String server_url = "https://37668f7b-a5c8-475c-821b-06324c4610a1.mock.pstmn.io/admin";
//    String server_url = "http://coms-309-060.class.las.iastate.edu:8080/meetinglocation/create";
//    String server_url_list = "http://coms-309-060.class.las.iastate.edu:8080/announcements";



    private ImageButton search;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);



        search = findViewById(R.id.searchBtn);


        builder = new AlertDialog.Builder(MainFeed.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        NavigationView navigationView = findViewById(R.id.nav_view);
        nDrawerLayout = findViewById(R.id.drawer);
        navigationView.setItemIconTintList(null);

        search = findViewById(R.id.searchBtn);



        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {

            VectorDrawableCompat indicator = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(), R.color.darkGrey, getTheme()));

            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);


        }

//        refreshBtn = findViewById(R.id.refreshBtn);
//        refreshBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                refreshContent();
//            }
//        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);

            }
        });


        /** If a certain screen is pressed, it will go to that certain screen.
         *
         */
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                itemSelected = item.toString();
                Intent intent;
                switch (itemSelected) {
//                    case "Donation":
//                        intent = new Intent(getApplicationContext(), DonationsActivity.class);
//                        startActivity(intent);
//                        break;
                    case "Friends":
                        intent = new Intent(getApplicationContext(), FriendFeatureActivity.class);
                        startActivity(intent);
                        break;
//                    case "Organization Auction":
//                        // Handle click on the fourth item
//                        intent = new Intent(getApplicationContext(), AuctionOrganizationActivity.class);
//                        startActivity(intent);
//                        break;
                    case "Auction":
                        intent = new Intent(getApplicationContext(), AuctionActivity.class);
                        startActivity(intent);
                        break;
                    case "Profile":
                        // Handle click on the first item
                        intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent);
                        break;
                    case "Sell":
                        // Handle click on the second item
                        intent = new Intent(getApplicationContext(), CreatePostActivity.class);
                        startActivity(intent);
                        break;
                    case "Inbox":
                        // Handle click on the third item
//                        alreadyConnected = true;
                        intent = new Intent(getApplicationContext(), InboxActivity.class);
                        startActivity(intent);

                        break;
                    case "Announcements":
                        // Handle click on the fourth item
                        intent = new Intent(getApplicationContext(), ViewAnnouncementsGenUser.class);
                        startActivity(intent);
                        break;
                    case "Settings":
                        // Handle click on the fourth item
                        intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intent);
                        break;
                }

                // Close the navigation drawer after handling the click
                nDrawerLayout.closeDrawers();

                return true; // Return true to indicate that the item is selected
            }
        });

        mRecyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearManager);




        mPostAdapter = new PostAdapter(mPostList, new PostAdapter.OnItemClickListener() {
            @Override public void onItemClick(PostItemObject item) {
                Log.d("Hi"," Bye");
                Intent intent = new Intent(getApplicationContext(), PostDetailActivity.class);
                intent.putExtra("id", String.valueOf(item.getPostID())); // +1 because the online example doesnt have "https://jsonplaceholder.typicode.com/users/0", just for demostration
                startActivity(intent);
            }
        });

        fetchPosts();

    }

    private void refreshContent() {
        // Perform actions to refresh the content here
        // For example, reload data from the server or reset the RecyclerView adapter
        mPostList.clear(); // Clear the current list of posts
        mPostAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
        fetchPosts(); // Fetch new posts from the server
    }

    private void fetchPosts() {
        mPostList.clear();
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, Const.URL_GET_ALL_POSTS, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("posts");
                        for (int i = jsonArray.length()-1; i >= 0; i--) {
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
                            mPostList.add(new PostItemObject(picture1,picture2,picture3,picture4,picture5,picture6,title,price,auction, description,userName,id));
                            Log.d("Post made with this information: ", picture1);
                        }


                        mRecyclerView.setAdapter(mPostAdapter);
                        mPostAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
            // Handle error
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }


    /**
     * Making image request
     * */
    private void makeImageRequest() {

        ImageRequest imageRequest = new ImageRequest(
                URL_IMAGE,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Display the image in the ImageView
                        imageView.setImageBitmap(response);

                    }
                },
                0, // Width, set to 0 to get the original width
                0, // Height, set to 0 to get the original height
                ImageView.ScaleType.FIT_XY, // ScaleType
                Bitmap.Config.RGB_565, // Bitmap config

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        Log.e("Volley Error", error.toString());
                    }
                }
        );

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(imageRequest);
    }

}


