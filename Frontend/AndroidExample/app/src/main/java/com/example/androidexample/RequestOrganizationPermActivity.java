package com.example.androidexample;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.Post.PostItemObject;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import android.widget.Button;

//need some method to pull existing users
public class RequestOrganizationPermActivity extends AppCompatActivity implements ReqPermAcceptedListener {

    private Toolbar toolbar;
    private ListView listViewFriendRequests;

    private List<UserReqPerm> friendRequestList;


    private String URL = "http://coms-309-060.class.las.iastate.edu:8080";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_req_org_perm);

        toolbar = findViewById(R.id.vwebtoolbar1);
//        listViewFriends = findViewById(R.id.FriendList);
        listViewFriendRequests = findViewById(R.id.orgReq);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        friendRequestList = new ArrayList<>();


//        // Call methods to make network requests
        fetchFriendRequestsData();
//        fetchOtherUsers();
    }


    //THIS WORKS
    private void fetchFriendRequestsData() {
        List<UserReqPerm> friendRequests = new ArrayList<>();
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, Const.URL_GET_UPGRADE_REQ, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("upgradeList");
                        for (int i = jsonArray.length()-1; i >= 0; i--) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String userName = jsonObject.getString("userName");
                            int id = jsonObject.getInt("id");
                            UserReqPerm friendRequest = new UserReqPerm(id, userName);

                            friendRequests.add(friendRequest);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
            // Handle error
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }



    @Override
    public void onOrgAccepted() {

    }

    @Override
    public void onOrgAccepted(String requesterUsername) {

    }
//
//    //no usages but i think this might have something to do w it
//    @Override
//    public void onFriendAccepted(String requesterUsername) {
//        String otherUsers = URL + "/friendrequests/" + LoginActivity.loginID +"/accept/" + requesterUsername;
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, otherUsers, null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        // Handle JSON response for friend requests data
//                        generalUsers = parseFriendsJson(response);
//                        friendList.addAll(generalUsers);
//
//                        ((ListFriends) listViewFriends.getAdapter()).updateFriendList(friendList);
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Handle errors
//                        Toast.makeText(RequestOrganizationPermActivity.this, "Error fetching friend requests data", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//        queue.add(jsonArrayRequest);
//
//    }



}