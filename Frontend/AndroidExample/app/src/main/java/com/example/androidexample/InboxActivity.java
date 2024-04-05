package com.example.androidexample;


// Import necessary Android classes and libraries
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;


import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.androidexample.SetLocationActivity;


/**
 * The InboxActivity class chat represents the chat feature of the application.
 * It extends AppCompatActivity and implements WebSocketListener interface.
 */
public class InboxActivity extends AppCompatActivity implements WebSocketListener {


//    private String serverUrl = "ws://10.0.2.2:8080/chat/";

    String serverUrl = "http://coms-309-060.class.las.iastate.edu:8443/chat/%7B" + LoginActivity.username + "%7D";


//    private boolean isWebSocketConnected = false;
    private ImageButton sendBtn, setLocation;

    private EditText msgEtx;

    private TextView msgTv;
    private Toolbar toolbar;

    private ActivityResultLauncher<Intent> setLocationLauncher;
    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied in
     *                           onSaveInstanceState(Bundle). Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to the layout defined in activity_inbox.xml
        setContentView(R.layout.activity_inbox);

        sendBtn = findViewById(R.id.sendBtn);

        msgEtx = findViewById(R.id.msgEdt);

        msgTv = findViewById(R.id.tx1);

        toolbar = findViewById(R.id.vwebtoolbar1);

        setLocation = findViewById(R.id.setLocationButton);


        setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SetLocationActivity.class);
                setLocationLauncher.launch(intent);
            }
        });

        setLocationLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            // Handle the result here
                            if (result.getData() != null) {
                                double latitude = result.getData().getDoubleExtra("latitude", 0.0);
                                double longitude = result.getData().getDoubleExtra("longitude", 0.0);
                                // Now you can use these coordinates as needed
                                String coordinates =  "!location " + latitude + " " + longitude;
//                                msgEtx.setText(coordinates);
                                WebSocketManager.getInstance().sendMessage(coordinates);

                            }
                        }
                    }
                });

            // Connect to WebSocket
            WebSocketManager.getInstance().connectWebSocket(serverUrl);
            WebSocketManager.getInstance().setWebSocketListener(InboxActivity.this);

//            isWebSocketConnected = true; // Set the flag to true indicating that WebSocket connection is established
//        }

        /* send button listener */
        sendBtn.setOnClickListener(v -> {
            try {
                // send message
                WebSocketManager.getInstance().sendMessage(msgEtx.getText().toString());
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage().toString());
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



    // WebSocketListener interface methods




    /**
     * Method called when a message is received via WebSocket.
     *
     * @param message The received message
     */
    public void onWebSocketMessage(String message) {

        /**
         * In Android, all UI-related operations must be performed on the main UI thread
         * to ensure smooth and responsive user interfaces. The 'runOnUiThread' method
         * is used to post a runnable to the UI thread's message queue, allowing UI updates
         * to occur safely from a background or non-UI thread.
         */
        // Log statement to indicate that the method is being called
//        Log.d("WebSocketMessage", "Received message: " + message);

        // Update the UI on the main thread
        runOnUiThread(() -> {

            // Update the UI with the received message
            // In case of location data, call the method to handle the JSON data
            if (message.startsWith("{")) {
                // JSON data received, handle it as location data
                handleLocationData(message);
            } else {
                // Regular text message, update UI accordingly
                String s = msgTv.getText().toString();
                msgTv.setText(s + "\n" + message);
            }

        });

    }


private void handleLocationData(String jsonData) {
    try {
        JSONObject locationJson = new JSONObject(jsonData);
        double latitude = locationJson.getDouble("latitude");
        double longitude = locationJson.getDouble("longitude");

        Log.d("LocationData", "Latitude: " + latitude + ", Longitude: " + longitude);

        SetLocationActivity.setPoint = new GeoPoint(latitude, longitude);

    } catch (JSONException e) {
        e.printStackTrace();
    }
}

    private String extractUsernameFromMessage(String message) {
        try {
            // Split the message by space to separate the username and message content
            String[] parts = message.split(" ", 2);
            // The sender's username is the first part of the split
            String username = parts[0];
            return username;
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }


    /**
     * Method called when WebSocket connection is closed.
     *
     * @param code   The code indicating the reason for closure
     * @param reason The reason for closure
     * @param remote Whether the closure was initiated by the remote endpoint
     */
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
//            isWebSocketConnected = false; // Reset the flag to false indicating that WebSocket connection is closed

        });
    }

    /**
     * Method called when there's an error with WebSocket connection.
     *
     * @param ex The exception representing the error
     */
    @Override
    public void onWebSocketError(Exception ex) {
        // Method called when there's an error with WebSocket connection
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {


    }
}
