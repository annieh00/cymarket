package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONException;
import org.json.JSONObject;

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
    private Button deletePostBtn;
    private Boolean auction;
    private String userName;
    private Boolean serverResponse;
    private int id;
    private ImageButton leftArrowBtn;
    private ImageButton rightArrowBtn;
    private Button editPostBtn;
    public static int pid;

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

        pid = Integer.parseInt(getIntent().getExtras().getString("id"));

        processURL(extras);
        makeJsonObjReq();
        ImageView imv = (ImageView) findViewById(R.id.imageSelView1);
        try {
            getImageAsJsonObjAndSetIt(imv,displayedImageIndex);
        }catch (Exception e){
            System.out.println("CALLING FAILED");
        }

        editPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditPostActivity.class);
                startActivity(intent);
            }
        });


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
                }else if(displayedImageIndex == 0){
                    //make it to 6 so that it feels like the user is rolling through the pictures
                    displayedImageIndex = 6;
                }
                getImageAsJsonObjAndSetIt(imv,displayedImageIndex);
            }
        });

        deletePostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePost();
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

                            if (userName.equals(LoginActivity.username) || LoginActivity.permission == 0){
                                deletePostBtn.setVisibility(View.VISIBLE);
                                editPostBtn.setVisibility(View.VISIBLE);
                            }else{
                                deletePostBtn.setVisibility(View.GONE);
                                editPostBtn.setVisibility(View.GONE);
                            }


                            titleTxtView.setText(titleTxt);
                            priceTxtView.setText(String.valueOf(price));
                            descriptionTxtView.setText(description);

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