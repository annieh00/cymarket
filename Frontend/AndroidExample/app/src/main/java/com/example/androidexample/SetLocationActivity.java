package com.example.androidexample;

// Import necessary Android classes and libraries
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomsheet.BottomSheetDialog;


/**
 * The SetLocationActivity class allows users to set a location on the map.
 * It extends AppCompatActivity.
 */
public class SetLocationActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1; // Request code for permissions
    private MapView map = null; // MapView object
    private Marker marker; // Marker object for indicating the selected location

    private double markerLatitude; // Latitude of the marker
    private double markerLongitude; // Longitude of the marker

    private BottomSheetDialog dialog; // Declare dialog as a class-level variable

    String server_url_create = "http://coms-309-060.class.las.iastate.edu:8080/chat/userName123";

    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load/initialize the osmdroid configuration
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        // Inflate layout and create the map
        setContentView(R.layout.activity_set_location);
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        // Request necessary permissions
        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });

        // Initialize map controller
        IMapController mapController = map.getController();
        mapController.setZoom(17.0); // Set zoom level

        // Set center point to Ames campus location
        GeoPoint amesCampus = new GeoPoint(42.0267, -93.6465);
        mapController.setCenter(amesCampus);

        // Enable multi-touch controls for panning and zooming
        map.setMultiTouchControls(true);

        // Set up gesture detector for handling map interactions
        GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                // Handle single tap event by adding a marker
                GeoPoint point = (GeoPoint) map.getProjection().fromPixels((int) e.getX(), (int) e.getY());

                //i would send the json data here
                addMarker(point);
                return true;
            }
        });

        // Set onTouchListener to handle gesture detection
        map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume(); // Resume map rendering
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause(); // Pause map rendering
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Handle permission request result
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permissions[i]);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Requests permissions if necessary.
     *
     * @param permissions The array of permissions to request.
     */
    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Adds a marker to the map at the specified GeoPoint.
     *
     * @param point The GeoPoint at which to add the marker.
     */
    private void addMarker(GeoPoint point) {
        if (marker != null) {
            map.getOverlays().remove(marker); // Remove existing marker
        }
        marker = new Marker(map);
        marker.setPosition(point);
        map.getOverlays().add(marker);
        map.invalidate(); // Refresh the map to display the marker

        // Store the latitude and longitude of the marker
        markerLatitude = point.getLatitude();
        markerLongitude = point.getLongitude();

        showBottomSheet();
    }

    private void showBottomSheet() {
        // Inflate the layout for the bottom sheet
        View view = getLayoutInflater().inflate(R.layout.modal_bottom_sheet_location, null);

        // Find buttons by their IDs
        Button btnYes = view.findViewById(R.id.yes);


        // Set onClickListener for the "Yes" button
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Marker Coordinates", "Latitude: " + markerLatitude + ", Longitude: " + markerLongitude);

                Intent intent = new Intent();
                intent.putExtra("latitude", markerLatitude);
                intent.putExtra("longitude", markerLongitude);
                setResult(RESULT_OK, intent);

                setLocation(markerLatitude, markerLongitude);
                finish(); // Close the SetLocationActivity

            }
        });

        // Set onClickListener for the "No" button


        // Create the BottomSheetDialog
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);

        // Show the dialog
        dialog.show();
    }

    private void setLocation(double markerLatitude, double markerLongitude) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("x", markerLatitude);
            jsonBody.put("y", markerLongitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, server_url_create, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                builder.setTitle("Server Response");
                try {
                    builder.setMessage("Response " + response.getString("status"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Handle positive button click
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SetLocationActivity.this, "Error....", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });

        VolleySingleton.getInstance(SetLocationActivity.this).addToRequestQueue(jsonObjReq);
    }


}