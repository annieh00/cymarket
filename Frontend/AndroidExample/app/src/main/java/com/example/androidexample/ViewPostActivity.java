package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewPostActivity extends AppCompatActivity {

    private Button btnJsonArrReq;
    private ListAdapter adapter;
    private ListView listView;
    private static final String URL_JSON_ARRAY = "https://07537acc-da80-4457-8b10-ff9e97cbea07.mock.pstmn.io/testCreatePost";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

//        btnJsonArrReq = findViewById(R.id.btnJsonArr);
//        listView = findViewById(R.id.listView);
//
//        // Initialize the adapter with an empty list (data will be added later)
//        adapter = new ListAdapter(this, new ArrayList<>());
//        listView.setAdapter(adapter);
//
//        btnJsonArrReq.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(Const.URL_POSTS + " ");
//                jsonParse();
//            }
//        });
//    }
//
//    /**
//     * Making json array request
//     * */
//    private void jsonParse() {
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Const.URL_POSTS, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray jsonArray = response.getJSONArray("users");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject users = jsonArray.getJSONObject(i);
//                        String title = users.getString("title");
//                        String description = users.getString("description");
////                        int age = users.getInt("age");
//                        int id = users.getInt("id");
//
//                        // Create a ListItemObject and add it to the adapter
//                        ListItemObject item = new ListItemObject("title", "description", "id");
//                        adapter.add(item);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//        VolleySingleton.getInstance(getApplicationContext());
    }
}