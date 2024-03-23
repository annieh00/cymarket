package com.example.androidexample;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.java_websocket.handshake.ServerHandshake;

import android.content.Intent;
//import android.content.res.Configuration;
import android.location.GpsStatus.Listener;
import android.os.Bundle;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.MinimapOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;


public class InboxActivity extends AppCompatActivity implements WebSocketListener  {
//    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
//    private MapView map = null;
//
//    private MinimapOverlay mMinimapOverlay;
//
    private Button btnShowMap;
    //    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_inbox);
        //handle permissions first, before map is created. not depicted here

        //load/initialize the osmdroid configuration, this can be done
//        Context ctx = getApplicationContext();
//        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's
        //tile servers will get you banned based on this string

        //inflate and create the map
        setContentView(R.layout.activity_inbox);

//        map = (MapView) findViewById(R.id.map);
//        map.setTileSource(TileSourceFactory.MAPNIK);
        btnShowMap = findViewById(R.id.btn_show_map);

        // Set click listener for the button
        btnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                // Hide the button when the map is shown
//                btnShowMap.setVisibility(View.GONE);
//                // Show the map when the button is clicked
//                map.setVisibility(View.VISIBLE);
//
//                // Request necessary permissions
//                requestPermissionsIfNecessary(new String[]{
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                });
//
//                // Set map controller to default viewpoint
//                IMapController mapController = map.getController();
//                mapController.setZoom(17.0); // Zoom level (adjust as needed)
//                GeoPoint startPoint = new GeoPoint(42.0267, -93.6465); // Coordinates for Ames, Iowa (Iowa State campus)
//                mapController.setCenter(startPoint);


                Intent intent = new Intent(getApplicationContext(), SetLocationActivity.class);
                startActivity(intent);
//
//        requestPermissionsIfNecessary(new String[]{
//                // if you need to show the current location, uncomment the line below
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                // WRITE_EXTERNAL_STORAGE is required in order to show the map
//
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
            }
        });
//        addIconsToMap();
        //code for minimap in the corner
//        DisplayMetrics dm = getResources().getDisplayMetrics();
//        mMinimapOverlay = new MinimapOverlay(this, map.getTileRequestCompleteHandler());
//        mMinimapOverlay.setWidth(dm.widthPixels / 5);
//        mMinimapOverlay.setHeight(dm.heightPixels / 5);
//        map.getOverlays().add(mMinimapOverlay);
    }

//        @Override
//        public void onResume() {
//            super.onResume();
//            //this will refresh the osmdroid configuration on resuming.
//            //if you make changes to the configuration, use
//            //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//            //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
//            map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
//        }
//
//        @Override
//        public void onPause() {
//            super.onPause();
//            //this will refresh the osmdroid configuration on resuming.
//            //if you make changes to the configuration, use
//            //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//            //Configuration.getInstance().save(this, prefs);
//            map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
//        }
//
//        @Override
//        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//            ArrayList<String> permissionsToRequest = new ArrayList<>();
//            for (int i = 0; i < grantResults.length; i++) {
//                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
//                    permissionsToRequest.add(permissions[i]);
//                }
//            }
//            if (!permissionsToRequest.isEmpty()) {
//                ActivityCompat.requestPermissions(
//                        this,
//                        permissionsToRequest.toArray(new String[0]),
//                        REQUEST_PERMISSIONS_REQUEST_CODE);
//            }
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Add this line
//        }
//
//        private void requestPermissionsIfNecessary(String[] permissions) {
//            ArrayList<String> permissionsToRequest = new ArrayList<>();
//            for (String permission : permissions) {
//                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
//                    permissionsToRequest.add(permission);
//                }
//            }
//            if (!permissionsToRequest.isEmpty()) {
//                ActivityCompat.requestPermissions(
//                        this,
//                        permissionsToRequest.toArray(new String[0]),
//                        REQUEST_PERMISSIONS_REQUEST_CODE);
//            }
//        }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {

    }

    @Override
    public void onWebSocketMessage(String message) {

    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onWebSocketError(Exception ex) {

    }

}
