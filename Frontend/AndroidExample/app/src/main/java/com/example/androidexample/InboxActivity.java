package com.example.androidexample;

// Import necessary Android classes and libraries
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

// Import WebSocket handshake class
import org.java_websocket.handshake.ServerHandshake;

import android.content.Intent;
import android.location.GpsStatus.Listener;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;

// Import necessary AndroidX classes
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

// Import necessary osmdroid classes
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.MinimapOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

/**
 * The InboxActivity class represents the main activity of the application.
 * It extends AppCompatActivity and implements WebSocketListener interface.
 */
public class InboxActivity extends AppCompatActivity implements WebSocketListener {

    private Button btnShowMap; // Declare Button variable

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

        // Initialize the button by finding it in the layout by its id
        btnShowMap = findViewById(R.id.btn_show_map);

        // Set click listener for the button
        btnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to SetLocationActivity
                Intent intent = new Intent(getApplicationContext(), SetLocationActivity.class);
                // Start the activity
                startActivity(intent);
            }
        });
    }

    // WebSocketListener interface methods

    /**
     * Method called when WebSocket connection is opened.
     *
     * @param handshakedata Information about the handshake
     */
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        // Method called when WebSocket connection is opened
    }

    /**
     * Method called when a message is received via WebSocket.
     *
     * @param message The received message
     */
    @Override
    public void onWebSocketMessage(String message) {
        // Method called when a message is received via WebSocket
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
        // Method called when WebSocket connection is closed
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
}