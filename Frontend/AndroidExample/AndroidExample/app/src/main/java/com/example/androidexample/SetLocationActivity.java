package com.example.androidexample;

// Import necessary Android classes and libraries
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import java.util.ArrayList;

/**
 * The SetLocationActivity class allows users to set a location on the map.
 * It extends AppCompatActivity.
 */
public class SetLocationActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1; // Request code for permissions
    private MapView map = null; // MapView object
    private Marker marker; // Marker object for indicating the selected location

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
    }
}
