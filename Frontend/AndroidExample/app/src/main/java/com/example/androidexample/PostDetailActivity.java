package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.Fragment.SavedActivityFragment;
import com.example.androidexample.Post.PostAdapter;
import com.example.androidexample.Post.PostItemObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class PostDetailActivity extends AppCompatActivity {

    public static final String DOMAIN = "http://coms-309-060.class.las.iastate.edu:8080";



    public String actualPostURL = Const.URL_GET_ALL_POSTS;
    private String URL_IMAGE = "http://sharding.org/outgoing/temp/testimg3.jpg";
    private String URL_JSON_OBJECT = "https://jsonplaceholder.typicode.com/users/";
    private String titleTxt;
    private TextView titleTxtView;
    private int price;
    private TextView priceTxtView;
    private String description;
    private TextView descriptionTxtView;
    private int postID;
    private Button deletePostBtn;
    private Boolean auction;
    private Boolean donation;
    private String userName;
    private Boolean serverResponse;
    private int id;
    private ImageButton leftArrowBtn;
    private ImageButton rightArrowBtn;
    private Button editPostBtn;
    public static int pid;
    private int imageNum = 0;
    private TextView authorOfPostTxtView;

    private ImageButton bookmarkBtn;
    private boolean isBookmarked = false;

    private int displayedImageIndex = 1;

    private void processURL(Bundle extras){
        int i = Const.URL_GET_ALL_POSTS.lastIndexOf("/");
        if (Const.URL_GET_ALL_POSTS.charAt(i+1) >= '0' && Const.URL_GET_ALL_POSTS.charAt(i+1) <= '9'){
            actualPostURL = Const.URL_GET_ALL_POSTS.substring(0,i)+"/"+extras.getString("id");
        }else{
            actualPostURL += ("/" + extras.getString("id"));
        }
        URL_JSON_OBJECT += extras.getString("id");
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_item);
        Bundle extras = getIntent().getExtras();
        titleTxtView = findViewById(R.id.titleTxt);
        priceTxtView = findViewById(R.id.priceTxt);
        descriptionTxtView = findViewById(R.id.descriptionTxt);
        leftArrowBtn = findViewById(R.id.leftArrowBtn);
        rightArrowBtn = findViewById(R.id.rightArrowBtn);
        deletePostBtn = findViewById(R.id.deletePostBtn);
        editPostBtn = findViewById(R.id.editPostBtn);
        authorOfPostTxtView = findViewById(R.id.authorOfPostTxt);
        bookmarkBtn = findViewById(R.id.bookmarkBtn);

        pid = Integer.parseInt(getIntent().getExtras().getString("id"));
        getImageNum();


        processURL(extras);
        makeJsonObjReq();
        ImageView imv = (ImageView) findViewById(R.id.imageSelView1);

        try {
            getImageAsJsonObjAndSetIt(imv,displayedImageIndex);
        }catch (Exception e){
            System.out.println("CALLING FAILED");
        }

        Toolbar t = (Toolbar)findViewById(R.id.toolbar);

        t.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                if (LoginActivity.permission == 0){
                    Intent intent = new Intent(PostDetailActivity.this, MainFeedAdmin.class);
                    startActivity(intent);
                } else if (LoginActivity.permission == 1){
                    Intent intent = new Intent(PostDetailActivity.this, MainFeedOrganizer.class);
                    startActivity(intent);
                } else if (LoginActivity.permission == 2){
                    Intent intent = new Intent(PostDetailActivity.this, MainFeed.class);
                    startActivity(intent);
                }

            }
        });

        editPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditPostActivity.class);
                startActivity(intent);
            }
        });

        checkBookMarkStatus();

        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle bookmark status
                isBookmarked = !isBookmarked;

                // If the bookmark is clicked
                if (isBookmarked) {
                    // Set the filled bookmark drawable
                    bookmarkBtn.setImageResource(R.drawable.baseline_bookmark_border_filled_24);
                    // Add the post to the bookmark list
                    addPostToArrayList();
                } else {
                    // If the bookmark is unclicked, set the empty bookmark drawable
                    bookmarkBtn.setImageResource(R.drawable.baseline_bookmark_border_24);
                    // Remove the post from the bookmark list
                    unbookmarkPost();
                }
            }
        });


        rightArrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(displayedImageIndex < 1 || displayedImageIndex > 6){
                    return;
                }

                if(displayedImageIndex  < imageNum){
                    displayedImageIndex++;

                }else{
                    //make it to 1 so that it feels like the user is rolling through the pictures
                    displayedImageIndex = 1;
                }

                getImageAsJsonObjAndSetIt(imv,displayedImageIndex);

            }
        });

        leftArrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(displayedImageIndex < 1 || displayedImageIndex > 6){
                    return;
                }
                if(displayedImageIndex  > 1){
                    displayedImageIndex--;
                }else{
                    //make it the number of images so that it feels like the user is rolling through the pictures
                    displayedImageIndex = imageNum;
                }
                getImageAsJsonObjAndSetIt(imv,displayedImageIndex);


            }
        });

        deletePostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePost();

                if (LoginActivity.permission == 0){
                    Intent intent = new Intent(PostDetailActivity.this, MainFeedAdmin.class);
                    startActivity(intent);
                } else if (LoginActivity.permission == 1){
                    Intent intent = new Intent(PostDetailActivity.this, MainFeedOrganizer.class);
                    startActivity(intent);
                } else if (LoginActivity.permission == 2){
                    Intent intent = new Intent(PostDetailActivity.this, MainFeed.class);
                    startActivity(intent);
                }

            }
        });

        //user that made the post
//        TextView txtRegister = (TextView)findViewById(R.id.authorOfPostTxt);
        authorOfPostTxtView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(PostDetailActivity.this, OtherProfileActivity.class);
                intent.putExtra("userName", userName);
                startActivity(intent);  // go to other profile
            }
        });


    }

    private void checkBookMarkStatus() {

        String url = DOMAIN + "/bookmarks/" + LoginActivity.loginID;





    }

    private void unbookmarkPost() {
        String url = DOMAIN +"/bookmarks/" + LoginActivity.loginID + "/remove/" + pid;

        Log.d("url" , url);

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle successful response
                        Log.d("Response", response.toString());
                        // You can perform any further actions here after the request is successful
                        //update of adapter not working here
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.d("Error", error.toString());
                        // You can show an error message to the user or perform any other error handling
                    }
                });

        // Add the request to the RequestQueue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }

    private void addPostToArrayList() {

        String url = DOMAIN +"/bookmarks/" + LoginActivity.loginID + "/add/" + pid;

        Log.d("url" , url);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle successful response
                        Log.d("Response", response.toString());
                        // You can perform any further actions here after the request is successful
                        //update of adapter not working here
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.d("Error", error.toString());
                        // You can show an error message to the user or perform any other error handling
                    }
                });

        // Add the request to the RequestQueue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }




    /**
     * decoding base64 string encoded image
     * @param base64Image
     * @return Bitmap
     */
    public static Bitmap decodeBase64ToBitmap(String base64Image) {
        byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    /**
     * Making json object request
     */
    private void makeJsonObjReq() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                actualPostURL,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ACTUAL POST INFORMATION LLLLLL", response.toString());
                        try {
                            titleTxt = response.getString("title");
                            price = response.getInt("price");
                            auction = response.getBoolean("isAuction");
                            description = response.getString("description");
                            userName = response.getString("userName");
                            id = response.getInt("id");
                            donation = response.getBoolean("isDonation");


                            Log.d("userName of the author:, username of the current user:", userName+ LoginActivity.username);
                            Log.d("permissin of the current user:", String.valueOf(LoginActivity.permission));
                            if (userName.equals(LoginActivity.username) || LoginActivity.permission == 0){
                                deletePostBtn.setVisibility(View.VISIBLE);
                                editPostBtn.setVisibility(View.VISIBLE);
                            }else{
                                deletePostBtn.setVisibility(View.GONE);
                                editPostBtn.setVisibility(View.GONE);
                            }

                            authorOfPostTxtView.setText("Author: " + userName);
                            titleTxtView.setText(titleTxt);
                            if (donation != true){
                                priceTxtView.setText("$" + String.valueOf(price));
                            }
                            descriptionTxtView.setText(description);
//                            Log.d("JSON REQ TO GET POST" + response.toString());

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
    }

    /**
     * Making json object request
     */
    private void deletePost() {
        JSONObject jsonObject = new JSONObject();
        try {

            //input your API parameters
            jsonObject.put("userName", userName);
            jsonObject.put("id", Integer.parseInt(getIntent().getExtras().getString("id")));
            Log.d("JSON OBJ:", jsonObject.toString());
            System.out.println("userName: " + userName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,
                Const.URL_DELETE_POST,
                jsonObject, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            serverResponse = response.getBoolean("serverResponse");

//                            Bundle extras =
                            for (int i = 0; i < MainFeed.mPostList.size(); i++){
                                if (MainFeed.mPostList.get(i).getPostID() == pid){
                                    MainFeed.mPostList.remove(i);
                                    break;
                                }
                            }


                            ((PostAdapter) MainFeed.mPostAdapter).notifyDataSetChanged();

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
    }

    //image index has to be from 1~6
    private void getImageAsJsonObjAndSetIt(ImageView imv, int imageIndex) {
        if(imageIndex < 1 || imageIndex > 6){return;}

        Bundle extras = getIntent().getExtras();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                "http://coms-309-060.class.las.iastate.edu:8080/image/" +extras.getString("id") +"/" +imageIndex,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            String encodedString = response.getString("image");
                            if(encodedString == null || encodedString.length() == 0 || encodedString.equals("")){
                                return;
                            }

                            Bitmap bm = decodeBase64ToBitmap(encodedString);
                            bm = Bitmap.createScaledBitmap(bm,150,150,false);
                            imv.setImageBitmap(bm);
                        } catch (JSONException e) {
                            //no json was in the response, which means that the user does not have the image with index

                            return;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
    }

//    private void getImageNum(String url){
//        Bundle extras = getIntent().getExtras();
//        for(int i = 1; i < 7; i++){
//            if ((url + extras.getString("id") +"/" + i) != "null"){
//                imageNum++;
//            }
//        }
//    }
    //image index has to be from 1~6
    private void getImageNum() {
//        if(imageNum < 1 || imageNum > 6){return;}
//        imageNum = 1; // Reset or initialize imageNum correctly
        Bundle extras = getIntent().getExtras();
        String postId = extras.getString("id");
//        Bundle extras = getIntent().getExtras();
        for(int i = 1; i < 7; i++){
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.GET,
                    "http://coms-309-060.class.las.iastate.edu:8080/image/" + postId +"/" + i,
                    null, // Pass null as the request body since it's a GET request
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Volley Response", response.toString());
                            try {
                                String encodedString = response.getString("image");
                                if (encodedString != null && !encodedString.isEmpty() && !encodedString.equals("null")) {
                                    imageNum++;
                                }


//                                Bitmap bm = decodeBase64ToBitmap(encodedString);
//                                bm = Bitmap.createScaledBitmap(bm,150,150,false);
//                                imv.setImageBitmap(bm);
                            } catch (JSONException e) {
                                //no json was in the response, which means that the user does not have the image with index

                                return;
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley Error", error.toString());
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                    return params;
                }
            };

            // Adding request to request queue
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
        }

    }




}