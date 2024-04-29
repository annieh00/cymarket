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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
//        String url = URL + "/friendrequests/" + LoginActivity.loginID + "/";
//        String url = URL + "/";


        String url = "https://07537acc-da80-4457-8b10-ff9e97cbea07.mock.pstmn.io/reqOrg";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Handle JSON response for friend requests data
                        friendRequestList = parseFriendsRequests(response);

                        // Populate ListView with friend requests data
                        ListOrganizationPermissions adapter = new ListOrganizationPermissions(RequestOrganizationPermActivity.this, friendRequestList, RequestOrganizationPermActivity.this);
                        listViewFriendRequests.setAdapter(adapter);



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Toast.makeText(RequestOrganizationPermActivity.this, "Error fetching friend requests data", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonArrayRequest);
    }

    //
    private List<UserReqPerm> parseFriendsRequests(JSONArray jsonArray) {
        List<UserReqPerm> friendRequests = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String status = jsonObject.getString("status");

                // Check if the status is "PENDING"
                if (status.equals("PENDING")) {
                    JSONObject senderObject = jsonObject.getJSONObject("sender");

                    // Extract sender information
                    String firstName = senderObject.getString("firstName");
                    String lastName = senderObject.getString("lastName");
                    int id = senderObject.getInt("id");
                    String username = senderObject.getString("userName");

                    // Create a FriendRequest object with sender information
                    UserReqPerm friendRequest = new UserReqPerm(firstName, lastName, id, username);

                    friendRequests.add(friendRequest);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return friendRequests;
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