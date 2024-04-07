package com.example.androidexample;

import com.android.volley.toolbox.HttpResponse;
import com.example.androidexample.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import com.android.volley.toolbox.ImageRequest;
import com.example.androidexample.Post.PostItemObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class PostDetailActivity extends AppCompatActivity {


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
    private Boolean auction;
    private String userName;
    private int id;
    private ImageButton leftArrowBtn;
    private ImageButton rightArrowBtn;
    private Button deleteBtn;
    private String deletePostURL = "http://coms-309-060.class.las.iastate.edu:8080/getSpecificPosts/" + LoginActivity.username;
    private Boolean isCurrentUserOwner;
    private int displayedImageIndex = 1;
    private void processURL(Bundle extras){
        int i = Const.URL_GET_ALL_POSTS.lastIndexOf("/");
        if (Const.URL_GET_ALL_POSTS.charAt(i+1) >= '0' && Const.URL_GET_ALL_POSTS.charAt(i+1) <= '9'){
            actualPostURL = Const.URL_GET_ALL_POSTS.substring(0,i)+"/"+extras.getString("id");
        }else{
            actualPostURL += ("/" + extras.getString("id"));
        }
//        URL_JSON_OBJECT += extras.getString("id");
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
        deleteBtn = findViewById(R.id.deletePostBtn);


        processURL(extras);
        makeJsonObjReq();
        ImageView imv = (ImageView) findViewById(R.id.imageSelView1);
        try {
            getImageAsJsonObjAndSetIt(imv,displayedImageIndex);
        }catch (Exception e){
            System.out.println("CALLING FAILED");
        }




        rightArrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(displayedImageIndex < 1 || displayedImageIndex > 6){
                    return;
                }

                if(displayedImageIndex  <= 6){
                    displayedImageIndex++;

                }else if(displayedImageIndex == 7){
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
                if(displayedImageIndex  >= 1 ){
                    displayedImageIndex--;
                }else if(displayedImageIndex == 1){
                    //make it to 6 so that it feels like the user is rolling through the pictures
                    displayedImageIndex = 6;
                }
                getImageAsJsonObjAndSetIt(imv,displayedImageIndex);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePosts();

            }
        });


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
    private void deletePosts() {
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, deletePostURL, null,
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
                        }

//                        mRecyclerView.setAdapter(mPostAdapter);
//                        mPostAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            // Handle error
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
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
                        Log.d("Volley Response", response.toString());
                        try {
                            titleTxt = response.getString("title");
                            price = response.getInt("price");
                            auction = response.getBoolean("isAuction");
                            description = response.getString("description");
                            userName = response.getString("userName");
                            id = response.getInt("id");

                            titleTxtView.setText(titleTxt);
                            priceTxtView.setText(String.valueOf(price));
                            descriptionTxtView.setText(description);

                            //if the current user logged in is not the same as the owner of the post
                            if (!userName.equals(LoginActivity.username)){
                                isCurrentUserOwner = false;
                            }else{
                                //if the current user logged in is the same as the owner of the post
                                isCurrentUserOwner = true;
                            }

                            //set visibility based on ownership
                            //if the current user logged in is not the same as the owner of the post
                            if (!isCurrentUserOwner){
                                deleteBtn.setVisibility(View.GONE);
                            }else{

                                deleteBtn.setVisibility(View.VISIBLE);
                            }

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


}