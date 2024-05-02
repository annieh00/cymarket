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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.Post.PostAdapter;
import com.example.androidexample.Post.PostItemObject;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Main feed displays the current posts.
 */
public class OtherProfileActivity extends AppCompatActivity {

    private DrawerLayout nDrawerLayout;

    private String postsUsersURL;


    AlertDialog.Builder builder;
    public static final String URL_IMAGE = "http://10.0.2.2:8080/images/1";

    private ListAdapter adapter;
    private ListView listView;
    private String itemSelected;
    private String actualPostURL;



    /**
     *
     */
    private ImageView imageView;

    private List<String> dataList = new ArrayList<>();
    /**
     * this is a tag that is attached to the log
     */
    private String TAG = OtherProfileActivity.class.getSimpleName();


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
    private PostAdapter mPostAdapter;
    private RecyclerView mRecyclerView;
    ArrayList<PostItemObject> mPostList = new ArrayList<>();

    private float avgRating;

    /**
     * this is the tag itself
     */
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";

    String server_url_list = "http://coms-309-060.class.las.iastate.edu:8080/meetinglocation";

    String server_url_create = "http://coms-309-060.class.las.iastate.edu:8080/meetinglocation/create";



    String server_url_del = "http://coms-309-060.class.las.iastate.edu:8080/meetinglocation/del/";

    String server_url_update = "http://coms-309-060.class.las.iastate.edu:8080/meetinglocation/update/";

    private Button deleteBtn;
    private TextView nameTxt;
    boolean alreadyRated = false;


    private RatingBar ratingBar;

    private Button confirmRatingBtn;
    private String specificPostURL;

//    private float ratingOfUser;
    private String userNameOfAuthor;
    private float ratingOfUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

//        refreshBtn = findViewById(R.id.refreshBtn);
//        xCoord = findViewById(R.id.xInput);
//        yCoord = findViewById(R.id.yInput);
//        setLocationBtn = findViewById(R.id.locationButton);
//        seeCoordinates = findViewById(R.id.listCoords);
//        coordListing = findViewById(R.id.coordList);
//        deleteCoordButton = findViewById(R.id.deleteCoord);
//        id = findViewById(R.id.coordIdDelete);
//        updateLocationBtn = findViewById(R.id.updateCoord);
//        updatedX = findViewById(R.id.updateX);
//        updatedY = findViewById(R.id.updateY);

        nameTxt = findViewById(R.id.Name);
        alreadyRated();


        userNameOfAuthor = Objects.requireNonNull(getIntent().getExtras()).getString("userName");

//        Intent intent = getIntent();
//        if (intent != null) {
//            String receivedValue = intent.getStringExtra("userName");
//            // Use the receivedValue here
//        }

        //        Bundle extras = getIntent().getExtras();
//        String userNameOfAuthor = extras.getString("userName");
        specificPostURL = "http://coms-309-060.class.las.iastate.edu:8080/getSpecificPosts/" + userNameOfAuthor; //+ userName of the author


        nameTxt.setText(userNameOfAuthor);


        builder = new AlertDialog.Builder(OtherProfileActivity.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        NavigationView navigationView = findViewById(R.id.nav_view);
        nDrawerLayout = findViewById(R.id.drawer);
        navigationView.setItemIconTintList(null);


        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {

            VectorDrawableCompat indicator = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(), R.color.darkGrey, getTheme()));

            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);

        }
        confirmRatingBtn = findViewById(R.id.confirmRatingBtn);
        if (alreadyRated == true){
            confirmRatingBtn.setVisibility(View.GONE);
        }else{
            confirmRatingBtn.setVisibility(View.VISIBLE);
        }


        ratingBar = findViewById(R.id.rb_ratingBar);

        ratingBar.setStepSize(0.5f);


        // Set an OnRatingBarChangeListener to handle user input
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                // Handle the rating change, e.g., update it in the database
                // Update the user interface to reflect the new rating
                // This could involve displaying the selected rating to the user
                Toast.makeText(getApplicationContext(), "Rating: " + rating, Toast.LENGTH_LONG).show();
                String ratingString = String.valueOf(rating);
                Log.d("Rating: ", ratingString);
                ratingOfUser = rating;

            }
        });

        confirmRatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRating();
                confirmRatingBtn.setVisibility(View.GONE);

            }
        });



        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPostAdapter = new PostAdapter(mPostList, new PostAdapter.OnItemClickListener() {
            @Override public void onItemClick(PostItemObject item) {
                // intent to the detail activity
                Intent intent = new Intent(getApplicationContext(), PostDetailActivity.class);
                intent.putExtra("id", String.valueOf(item.getPostID())); // +1 because the online example doesnt have "https://jsonplaceholder.typicode.com/users/0", just for demostration
                startActivity(intent);
            }
        });

        fetchPosts();

    }

    private void sendRating() {
//        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject jsonObject = new JSONObject();
//        JSONObject body = new JSONObject();
        try {
            //input your API parameters
            jsonObject.put("userName", LoginActivity.username);
            jsonObject.put("score", ratingOfUser);
            jsonObject.put("description", "sdfklsdfl");
            Log.d("what im sending in rating", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_SET_USER_RATING + "/" + userNameOfAuthor, jsonObject, response -> {
            Log.d(TAG, response.toString());
            try {
                    avgRating = (float) response.getDouble("avgRating");
                    ratingBar.setRating(avgRating);
                    ratingBar.setIsIndicator(true);
                    alreadyRated = true;
            }catch (JSONException e) {

            }


        }, error -> {
            VolleyLog.d(TAG, "Error: " + error.getMessage());

        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }

        };

        //queue.add(jsonObjReq);
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
        //VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().start();


    }

    private void alreadyRated() {

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, Const.URL_GET_ALL_RATINGS, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("ratings");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String authorUsername = jsonObject.getString("authorUsername");
                            String revieweeUserName = jsonObject.getString("revieweeUserName");
                            if (authorUsername.equals(userNameOfAuthor) && revieweeUserName.equals(LoginActivity.username)){
                                alreadyRated = true;
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            // Handle error
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }


    private void fetchPosts() {
//        String url = "http://42b4cef6-ab22-4745-b3fe-4fa097c327da.mock.pstmn.io/getAllPosts";
//        JSONObject jsonObject2 = new JSONObject();
//        try {
//            //input your API parameters
//            jsonObject2.put("userName", LoginActivity.username);
//
//            //            Toast.makeText(LoginActivity.this, "got e and p", Toast.LENGTH_LONG).show();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, specificPostURL, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("posts");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            String picture1 = jsonObject.getString("picture1");
//                            String picture2 = jsonObject.getString("picture2");
//                            String picture3 = jsonObject.getString("picture3");
//                            String picture4 = jsonObject.getString("picture4");
//                            String picture5 = jsonObject.getString("picture5");
//                            String picture6 = jsonObject.getString("picture6");
                            String picture1Data = jsonObject.getString("picture1Data");
                            String picture1 = null;
                            String picture2 = null;
                            String picture3 = null;
                            String picture4 = null;
                            String picture5 = null;
                            String picture6 = null;

                            String title = jsonObject.getString("title");
                            int price = jsonObject.getInt("price");
                            Boolean auction = jsonObject.getBoolean("isAuction");
                            String description = jsonObject.getString("description");
                            String userName = jsonObject.getString("userName");
                            int id = jsonObject.getInt("id");
                            mPostList.add(new PostItemObject(picture1Data, picture1,picture2,picture3,picture4,picture5,picture6,title,price,auction, description,userName,id));
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

//    private void jsonParse(){
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Const.URL_GET_ALL_POSTS, null,
//                response -> {
//                    try {
//                        JSONArray jsonArray = response.getJSONArray("posts");
//                        for (int i = 0; i < jsonArray.length(); i++){
//
//                        }
//                    }catch(JSONException e){
//                        throw new RuntimeException(e);
//                    }
//                })
//
//    }


//    private void makeJsonArrayReq () {
//
//        coordListing.setText("");
//        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
//                Request.Method.GET,
//                server_url_list,
//                null, // Pass null as the request body since it's a GET request
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d("Volley Response", response.toString());
//
//
//
//                        StringBuilder stringBuilder = new StringBuilder();
//                        // Parse the JSON array and add data to the adapter
//                        for (int i = 0; i < response.length(); i++) {
//                            try {
//                                JSONObject jsonObject = response.getJSONObject(i);
//                                String x = jsonObject.getString("x");
//                                String y = jsonObject.getString("y");
//
//                                // Create a ListItemObject and add it to the adapter
////                                    ListItemObjectCoords item = new ListItemObjectCoords(x, y);
////                                    adapter.add(item);
//                                stringBuilder.append("X: ").append(x).append(", Y: ").append(y).append("\n");
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        coordListing.setText(stringBuilder.toString());
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
////                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
////                headers.put("Content-Type", "application/json");
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
//        // Adding request to request queue
//        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrReq);
//    }
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

//    public static final Uri EXTRA_postPicture1 = null;
//    public static final Uri EXTRA_postPicture2 = null;
//    public static final Uri EXTRA_postPicture3 = null;
//    public static final Uri EXTRA_postPicture4 = null;
//    public static final Uri EXTRA_postPicture5 = null;
//    public static final Uri EXTRA_postPicture6 = null;
//
//    public static final String EXTRA_postTitle = "postTitle";
//    public static final int EXTRA_postPrice = 0;
//    public static final String EXTRA_postDate = "date";
//    public static final String EXTRA_postCategory = "category";
//    public static final int EXTRA_postFlagCount = 0;
//    public static final Boolean EXTRA_postAuction = false;
//
//    public static final int EXTRA_userID = 0;
//
//    public static final int EXTRA_postID = 0;

    /**
     * this sets up an intent and send them Vote Poll Activity
     * @param position
     */
//    @Override
//    public void onItemClick(int position) {
//        Intent detailIntent = new Intent(this, MainFeed.class);
//        PostItemObject clickeditem = mPostList.get(position);
//        detailIntent.putExtra(String.valueOf(EXTRA_postPicture1), clickeditem.getPicture1());
//        detailIntent.putExtra(String.valueOf(EXTRA_postPicture2), clickeditem.getPicture2());
//        detailIntent.putExtra(String.valueOf(EXTRA_postPicture3), clickeditem.getPicture3());
//        detailIntent.putExtra(String.valueOf(EXTRA_postPicture4), clickeditem.getPicture4());
//        detailIntent.putExtra(String.valueOf(EXTRA_postPicture5), clickeditem.getPicture5());
//        detailIntent.putExtra(String.valueOf(EXTRA_postPicture6), clickeditem.getPicture6());
//        detailIntent.putExtra(EXTRA_postTitle, clickeditem.getTitle());
//        detailIntent.putExtra(String.valueOf(EXTRA_postPrice), clickeditem.getPrice());
//        detailIntent.putExtra(EXTRA_postDate, clickeditem.getDate());
//        detailIntent.putExtra(EXTRA_postCategory, clickeditem.getCategory());
//        detailIntent.putExtra(String.valueOf(EXTRA_postFlagCount), clickeditem.getFlagCount());
//        detailIntent.putExtra(String.valueOf(EXTRA_postAuction), clickeditem.getAuction());
//        detailIntent.putExtra(String.valueOf(EXTRA_userID), clickeditem.getUserID());
//        detailIntent.putExtra(String.valueOf(EXTRA_postID), clickeditem.getPostID());
//        startActivity(detailIntent);
//
//    }


}


