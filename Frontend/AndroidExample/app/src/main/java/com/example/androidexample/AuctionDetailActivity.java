package com.example.androidexample;


import static com.example.androidexample.LoginActivity.username;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import com.android.volley.toolbox.ImageRequest;

import java.util.HashMap;
import java.util.Map;



public class AuctionDetailActivity extends AppCompatActivity implements WebSocketListener{

    private TextView msgTv;
    private ImageView imageView;
    private TextView msgResponse;

    public String actualPostURL = Const.URL_AUCTION;
    private String URL_IMAGE = "http://sharding.org/outgoing/temp/testimg3.jpg";
    private String URL_JSON_OBJECT = "https://jsonplaceholder.typicode.com/users/";
    private EditText bidEditTxt;
    private TextView highestBidTxt;
    private Button confirmBtn;
    private volatile String incomingMessages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_item);
        Bundle extras = getIntent().getExtras();
        confirmBtn = findViewById(R.id.confirmBidBtn);
        bidEditTxt = (EditText) findViewById(R.id.bidEditTxt);
        highestBidTxt = (TextView) findViewById(R.id.highestBidTxt);


        imageView = (ImageView) findViewById(R.id.imageSelView1);
//        msgResponse = findViewById(R.id.msgResponse);
         msgTv = findViewById(R.id.tx1);

        int i = Const.URL_AUCTION.lastIndexOf("/");
        if (Const.URL_AUCTION.charAt(i+1) >= '0' && Const.URL_AUCTION.charAt(i+1) <= '9'){
            actualPostURL = Const.URL_AUCTION.substring(0,i)+"/"+extras.getString("id")+"/" + "helloWorld5";
            Log.d("Auction URL:", actualPostURL);
        }else{
            actualPostURL += ("/" + extras.getString("id"));
            Log.d("Auction URL:", actualPostURL);

        }
        URL_JSON_OBJECT += extras.getString("id");

        Log.d("userNmae:", username);

        WebSocketManager.getInstance().connectWebSocket("ws://coms-309-060.class.las.iastate.edu:8080/auction/5/helloWorld5");
        WebSocketManager.getInstance().setWebSocketListener(AuctionDetailActivity.this);
//        Log.d("UserName:", username);
//        makeImageRequest();
//        makeJsonObjReq();
        confirmBtn.setOnClickListener(v -> {
            try {
//                int highestBid;
////
//                if (incomingMessages.charAt(incomingMessages.lastIndexOf("$") + 1) == ' '){
//                    highestBid = Integer.parseInt(incomingMessages.substring(incomingMessages.lastIndexOf("$") + 2));
//                }else{// a number
//                    highestBid = Integer.parseInt(incomingMessages.substring(incomingMessages.lastIndexOf("$") + 1 ));
//                }
//                highestBidTxt.setText("Highest Bid: " + highestBid);

                // send message
                WebSocketManager.getInstance().sendMessage(bidEditTxt.getText().toString());
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage().toString());
            }
        });
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

    /**
     * Making json object request
     */
    private void makeJsonObjReq() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET, actualPostURL,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
//                        msgResponse.setText(response.toString());
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

        int highestBid;
        if(message.charAt(message.lastIndexOf("$")) != -1){
            if (message.charAt(message.lastIndexOf("$") + 1) == ' '){
                highestBid = Integer.parseInt(message.substring(message.lastIndexOf("$") + 2));
            }else{// a number
                highestBid = Integer.parseInt(message.substring(message.lastIndexOf("$") + 1 ));
            }
            highestBidTxt.setText("Highest Bid: " + highestBid);
        }
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();

            msgTv.setText(s + "\n"+message);
            incomingMessages = message;

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


}


