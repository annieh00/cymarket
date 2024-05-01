package com.example.androidexample;


import static com.example.androidexample.LoginActivity.username;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.androidexample.Auction.AuctionAdapter;

import java.util.HashMap;
import java.util.Map;



public class AuctionOrganizationDetailActivity extends AppCompatActivity implements WebSocketListener{
    private Boolean isCurrentUserOwner;

    private TextView msgTv;

    private ImageButton leftArrowBtn;
    private ImageButton rightArrowBtn;
    private TextView descriptionTxtView;
    public static int pid;
    private String winner;


    public String actualPostURL = Const.URL_AUCTION;
    private String URL_IMAGE = "http://sharding.org/outgoing/temp/testimg3.jpg";
    private String URL_JSON_OBJECT = "https://jsonplaceholder.typicode.com/users/";
    private EditText bidEditTxt;
    private TextView highestBidTxt;
    private Button confirmBtn;
    private Button closeAuctionBtn;
    private String titleTxt;
    private int price;
    private Boolean auction;
    private String description;
    private String userName;
    private TextView priceTxtView;
    private int id;
    private TextView titleTxtView;


    private volatile String incomingMessages;



    private String getRequest;
    private ImageView imv;
    private int displayedImageIndex = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_organization_item);
        Bundle extras = getIntent().getExtras();
        confirmBtn = findViewById(R.id.confirmBidBtn);
        bidEditTxt = (EditText) findViewById(R.id.bidEditTxt);
        leftArrowBtn = findViewById(R.id.leftArrowBtn);
        rightArrowBtn = findViewById(R.id.rightArrowBtn);
        imv = (ImageView) findViewById(R.id.imageSelView1);
        closeAuctionBtn = findViewById(R.id.closeAuctionBtn);
        titleTxtView = findViewById(R.id.titleTxtView);
//        priceTxtView = findViewById(R.id.priceTxt);
        descriptionTxtView = findViewById(R.id.descriptionTxtView);

        getRequest = Const.URL_GET_ALL_POSTS + "/" + extras.getString("id");
        makeJsonObjReq();

//        msgResponse = findViewById(R.id.msgResponse);
        msgTv = findViewById(R.id.tx1);

        pid = Integer.parseInt(getIntent().getExtras().getString("id"));

//        int i = Const.URL_AUCTION.lastIndexOf("/");
//        if (Const.URL_AUCTION.charAt(i+1) >= '0' && Const.URL_AUCTION.charAt(i+1) <= '9') {
        actualPostURL = Const.URL_AUCTION + "/" + extras.getString("id") + "/" + LoginActivity.username;
        Log.d("Auction URL:", actualPostURL);
//        }else{
//            actualPostURL += ("/" + extras.getString("id"));
//            Log.d("Auction URL:", actualPostURL);
//
//        }
//        }
        URL_JSON_OBJECT += extras.getString("id");

//        Log.d("userNmae:", username);
        try {
            getImageAsJsonObjAndSetIt(imv,displayedImageIndex);
        }catch (Exception e){
            System.out.println("CALLING FAILED");
        }

        WebSocketManager.getInstance().connectWebSocket("ws://coms-309-060.class.las.iastate.edu:8080/auction/"+extras.getString("id")+"/"+username);
        Log.d("actual auction url:", "ws://coms-309-060.class.las.iastate.edu:8080/auction/"+extras.getString("id")+"/"+username);
        WebSocketManager.getInstance().setWebSocketListener(AuctionOrganizationDetailActivity.this);


        confirmBtn.setOnClickListener(v -> {
            try {
                // send message
                WebSocketManager.getInstance().sendMessage(bidEditTxt.getText().toString());
//                Log.d("BID:", bidEditTxt.getText().toString());
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage().toString());
            }
        });


        closeAuctionBtn.setOnClickListener(v -> {
            try {
                closeAuction();
                if (LoginActivity.permission == 0){
                    Intent intent = new Intent(AuctionOrganizationDetailActivity.this, MainFeedAdmin.class);
                    startActivity(intent);
                } else if (LoginActivity.permission == 1){
                    Intent intent = new Intent(AuctionOrganizationDetailActivity.this, MainFeedOrganizer.class);
                    startActivity(intent);
                } else if (LoginActivity.permission == 2){
                    Intent intent = new Intent(AuctionOrganizationDetailActivity.this, MainFeed.class);
                    startActivity(intent);
                }

            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage().toString());
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
                }else if(displayedImageIndex == 1){
                    //make it to 6 so that it feels like the user is rolling through the pictures
                    displayedImageIndex = 6;
                }
                getImageAsJsonObjAndSetIt(imv,displayedImageIndex);
            }
        });
    }

    /**
     * Making json object request
     */
    private void makeJsonObjReq() {
        Log.d("GET REQUEST:", getRequest);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                getRequest,
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
                                closeAuctionBtn.setVisibility(View.VISIBLE);
                            }else{
                                closeAuctionBtn.setVisibility(View.GONE);
                            }
                            titleTxtView.setText(titleTxt);
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



    @Override
    public void onWebSocketMessage(String message) {
        /**
         * In Android, all UI-related operations must be performed on the main UI thread
         * to ensure smooth and responsive user interfaces. The 'runOnUiThread' method
         * is used to post a runnable to the UI thread's message queue, allowing UI updates
         * to occur safely from a background or non-UI thread.
         */
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();

            msgTv.setText(s + "\n"+message);
            incomingMessages = message;
            if (LoginActivity.username.equals(userName)){
                msgTv.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View view) {
                        Intent intent = new Intent(AuctionOrganizationDetailActivity.this, InboxActivity.class);
                        intent.putExtra("userToText", msgTv.toString().indexOf(" "));
                        startActivity(intent);  // go to SignupActivity
                    }
                });
            }

        });
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
        });
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {}

    @Override
    public void onWebSocketError(Exception ex) {}

    public static Bitmap decodeBase64ToBitmap(String base64Image) {
        byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void closeAuction() {
        JSONObject jsonObject = new JSONObject();
        try {

            //input your API parameters
//            jsonObject.put("userName", LoginActivity.username);
            jsonObject.put("id", pid);
            Log.d("JSON OBJ:", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,
                Const.URL_CLOSE_AUCTION,
                jsonObject, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            winner = response.getString("winner");
                            for (int i = 0; i < AuctionActivity.mPostList.size(); i++){
                                if (AuctionActivity.mPostList.get(i).getPostID() == pid){
                                    AuctionActivity.mPostList.remove(i);
                                    break;
                                }
                            }

                            ((AuctionAdapter) AuctionActivity.mPostAdapter).notifyDataSetChanged();


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
                            if(encodedString == null && encodedString.length() == 0 && encodedString.equals("")){

                            }

                            Bitmap bm = decodeBase64ToBitmap(encodedString);
                            bm = Bitmap.createScaledBitmap(bm,150,150,false);
                            imv.setImageBitmap(bm);
                        } catch (JSONException e) {
                            //no json was in the response, which means that the user does not have the image with index

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


