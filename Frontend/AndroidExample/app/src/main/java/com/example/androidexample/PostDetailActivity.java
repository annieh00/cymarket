package com.example.androidexample;

import com.example.androidexample.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.graphics.Bitmap;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import com.android.volley.toolbox.ImageRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class PostDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView msgResponse;

    public String actualPostURL = Const.URL_CREATE_POST;
    private String URL_IMAGE = "http://sharding.org/outgoing/temp/testimg3.jpg";
    private String URL_JSON_OBJECT = "https://jsonplaceholder.typicode.com/users/";
    private String picture1;
    private String picture2;
    private String picture3;
    private String picture4;
    private String picture5;
    private String picture6;
    private String titleTxt;
    private TextView titleTxtView;
    private int price;
    private TextView priceTxtView;
    private Boolean isAuction;
    private String description;
    private TextView descriptionTxtView;
    private int postID;
    private ArrayList<Bitmap> imageList;
private Boolean auction;
private String userName;
private int id;
private ImageButton leftArrowBtn;
    private ImageButton rightArrowBtn;
    private int currentImageIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_item);
        Bundle extras = getIntent().getExtras();
        titleTxtView = findViewById(R.id.titleTxt);
        imageView = findViewById(R.id.imageSelView1);
        priceTxtView = findViewById(R.id.priceTxt);
        descriptionTxtView = findViewById(R.id.descriptionTxt);
        leftArrowBtn = findViewById(R.id.leftArrowBtn);
        rightArrowBtn = findViewById(R.id.rightArrowBtn);


        //        imageView = (ImageView) findViewById(R.id.imgView);
//        msgResponse = findViewById(R.id.msgResponse);

        int i = Const.URL_GET_ALL_POSTS.lastIndexOf("/");
        if (Const.URL_GET_ALL_POSTS.charAt(i+1) >= '0' && Const.URL_GET_ALL_POSTS.charAt(i+1) <= '9'){
            actualPostURL = Const.URL_GET_ALL_POSTS.substring(0,i)+"/"+extras.getString("id");
        }else{
            actualPostURL += ("/" + extras.getString("id"));
        }
        URL_JSON_OBJECT += extras.getString("id");

        int j = 0;
        for (j = 0; j < 6; j++){
            makeImageRequest(Const.URL_IMAGES + extras.getString("id") + "/" + j);
        }
        imageView.setImageBitmap(imageList.get(currentImageIndex));


        rightArrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImageIndex++;
                if (currentImageIndex == 6){
                    currentImageIndex = 0;
                }
            }
        });
        makeJsonObjReq();
    }


    /**
     * Making image request
     * */
    private void makeImageRequest(String URL) {

//        imageList = new ArrayList<>();

        ImageRequest imageRequest = new ImageRequest(
                URL,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {

                        // Display the image in the ImageView
                        if (response != null && response.getByteCount() != 0){
//                            imageView.setImageBitmap(response);
                            imageList.add(response);
                        }

//                        imageView.setImageBitmap(response);

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
                            picture1 = response.getString("picture1");
                            picture2 = response.getString("picture2");
                            picture3 = response.getString("picture3");
                            picture4 = response.getString("picture4");
                            picture5 = response.getString("picture5");
                            picture6 = response.getString("picture6");
                            titleTxt = response.getString("title");
                            price = response.getInt("price");
                            auction = response.getBoolean("isAuction");
                            description = response.getString("description");
                            userName = response.getString("userName");
                            id = response.getInt("id");

                            titleTxtView.setText(titleTxt);
                            priceTxtView.setText(price);
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



}