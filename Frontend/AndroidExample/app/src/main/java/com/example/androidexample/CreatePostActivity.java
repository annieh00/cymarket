package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;

import com.android.volley.toolbox.Volley;
import com.example.androidexample.Const;
//import com.example.androidexample.Manifest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CreatePostActivity extends AppCompatActivity{
    private EditText titleEditText;
    private ImageButton addImageBtn;
    private EditText descriptionEditText;
    private Button cancelBtn;
    private Button postBtn;
    private String TAG = CreatePostActivity.class.getSimpleName();
    private EditText categoryEditTxt;
    private HorizontalScrollView images;
    private ImageView image1 = null;
    private ImageView image2 = null;
    private ImageView image3 = null;
    private ImageView image4 = null;
    private ImageView image5 = null;
    private ImageView image6 = null;

    private String title;
    private String description;
    private String category;
//    private static final String ROOT_URL = "http://seoforworld.com/api/v1/file-upload.php";
//    private static final int REQUEST_PERMISSIONS = 100;
//    private static final int PICK_IMAGE_REQUEST =1 ;
    private Bitmap bitmap;
    private String filePath;
//    ImageView imageView;
    TextView textView;

    String URL_POST_LOGIN_USER = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);            // link to Login activity XML

        /* initialize UI elements */
        //Text

        titleEditText = findViewById(R.id.Title);
        descriptionEditText = findViewById(R.id.DescriptionEditText);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);
        image5 = findViewById(R.id.image5);
        image6 = findViewById(R.id.image6);

        //Buttons
        cancelBtn = findViewById(R.id.cancel_button);    // link to login button in the Login activity XML
        postBtn = findViewById(R.id.post_button);  // link to signup button in the Login activity XML
        addImageBtn = findViewById(R.id.addImageButton); //link to add images


        /* click listener on signup button pressed */
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when post button is pressed, use intent to switch to Signup Activity */
                Intent intent = new Intent(CreatePostActivity.this, MainFeed.class);
                startActivity(intent);  // go to SignupActivity
            }
        });

        addImageBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){

            }
        });

        /* click listener on login button pressed */
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatePostActivity.this, MainFeed.class);
                startActivity(intent);  // go to SignupActivity
            }
        });
    }

    private void sendJsonObjReq() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject jsonObject = new JSONObject();
        JSONObject body = new JSONObject();
        try {
            //input your API parameters
            jsonObject.put("title", titleEditText.getText().toString());
            jsonObject.put("category", categoryEditTxt.getText().toString());
            jsonObject.put("description", descriptionEditText.getText().toString());
            jsonObject.put("picture1", titleEditText.getText().toString());
            jsonObject.put("picture2", titleEditText.getText().toString());
            jsonObject.put("picture3", titleEditText.getText().toString());
            jsonObject.put("picture4", titleEditText.getText().toString());
            jsonObject.put("picture5", titleEditText.getText().toString());
            jsonObject.put("picture6", titleEditText.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, URL_POST_LOGIN_USER, jsonObject, response -> {
            Log.d(TAG, response.toString());
            try {
                category = response.getString("category");
                title = response.getString("title");
                description = response.getString("description");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }




        }, error -> {
            VolleyLog.d(TAG, "Error: " + error.getMessage());
            Toast.makeText(CreatePostActivity.this, "User Not Found", Toast.LENGTH_LONG).show();
//            txtValidity = true;
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }

        };

        //queue.add(jsonObjReq);
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
        //VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().start();


    }

}


