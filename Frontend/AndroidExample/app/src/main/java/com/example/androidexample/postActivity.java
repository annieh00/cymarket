package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.toolbox.Volley;
//import com.example.androidexample.Manifest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class postActivity extends AppCompatActivity{

private int id;
    private Button deletePostBtn;
    private String TAG = postActivity.class.getSimpleName();

    private EditText deletePostInput;
    private EditText updateTitleEditTxt;
    private Button updatePostTitleBtn;

    private Boolean serverResponse;
    private EditText updateTitleIDEditTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_activity);            // link to Login activity XML

        /* initialize UI elements */
        //Text
        updatePostTitleBtn = findViewById(R.id.updateTitleBtn);
        updateTitleEditTxt = findViewById(R.id.titleEditTxt);
        deletePostBtn = findViewById(R.id.deletePostBtn);
        deletePostInput = findViewById(R.id.deletePostInput);
        updateTitleIDEditTxt = findViewById(R.id.idEditTxt);
        deletePostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePostByID();
            }
        });

        updatePostTitleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePostTitle();
            }
        });
    }


    private void updatePostTitle() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject jsonObject = new JSONObject();
        try {
            //input your API parameters
            jsonObject.put("id", updateTitleEditTxt.getText().toString());
            jsonObject.put("title", updateTitleIDEditTxt.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_UPDATE_POST, jsonObject, response -> {
            Log.d(TAG, response.toString());
            try {
                serverResponse = response.getBoolean("serverResponse");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if(serverResponse){
                Toast.makeText(postActivity.this, "Post updated successfully", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(postActivity.this, "Something was wrong", Toast.LENGTH_SHORT).show();
            }



        }, error -> {
            VolleyLog.d(TAG, "Error: " + error.getMessage());
            Toast.makeText(postActivity.this, "User Not Found", Toast.LENGTH_LONG).show();
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

        MySingleton.getInstance(postActivity.this).addToRequestQueue(jsonObjReq);
//        queue.add(jsonObjReq);
//        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
        //VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().start();


    }
    private void deletePostByID() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject jsonObject = new JSONObject();
        try {
            //input your API parameters
            jsonObject.put("id", deletePostInput.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_DELETE_POST, jsonObject, response -> {
            Log.d(TAG, response.toString());
            try {
                serverResponse = response.getBoolean("serverResponse");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

                if(serverResponse){
                    Toast.makeText(postActivity.this, "Post deleted successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(postActivity.this, "Something was wrong", Toast.LENGTH_SHORT).show();
                }



        }, error -> {
            VolleyLog.d(TAG, "Error: " + error.getMessage());
            Toast.makeText(postActivity.this, "User Not Found", Toast.LENGTH_LONG).show();
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

        MySingleton.getInstance(postActivity.this).addToRequestQueue(jsonObjReq);
//        queue.add(jsonObjReq);
//        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
        //VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().start();


    }

}


