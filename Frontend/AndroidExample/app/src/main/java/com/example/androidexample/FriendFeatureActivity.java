package com.example.androidexample;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
public class FriendFeatureActivity extends AppCompatActivity implements FriendAcceptedListener {

    private Toolbar toolbar;
    public static ListView listViewFriends;
    private ListView listViewFriendRequests;

    private ListView listViewGenUsers;
    private List<Friend> friendList;
    private List<Friend> friendRequestList;

    private List<Friend> generalUsers;

    private String URL = "http://coms-309-060.class.las.iastate.edu:8443";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_feature);

        toolbar = findViewById(R.id.vwebtoolbar1);
        listViewFriends = findViewById(R.id.FriendList);
        listViewFriendRequests = findViewById(R.id.FriendRequestList);
        listViewGenUsers = findViewById(R.id.generalUsers);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        friendList = new ArrayList<>();
        friendRequestList = new ArrayList<>();
        generalUsers = new ArrayList<>();


//        // Call methods to make network requests
        fetchFriendsData();
        fetchFriendRequestsData();
        fetchOtherUsers();
    }

    //WORKING!!!!
    private void fetchFriendsData() {
        String friends_url = URL + "/friends/" + LoginActivity.loginID + "/list";
//        String friends_url = URL + "/friendrequests/" + LoginActivity.username + "/";
//        String friends_url = "https://37668f7b-a5c8-475c-821b-06324c4610a1.mock.pstmn.io" + "/friends";
        //replace userName with LoginActivity.username
//        String friends_url = "https://37668f7b-a5c8-475c-821b-06324c4610a1.mock.pstmn.io" +"/friends/userName123";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, friends_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Handle JSON response for friends data
                        friendList = parseFriendsJson(response);

                        // Populate ListView with friends data
                        ListFriends adapter = new ListFriends(FriendFeatureActivity.this, friendList);
                        listViewFriends.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Toast.makeText(FriendFeatureActivity.this, "Error fetching friends data", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonArrayRequest);
    }


    //THIS WORKS
    private void fetchFriendRequestsData() {
        String url = URL + "/friendrequests/" + LoginActivity.loginID + "/";


//        String url = "https://37668f7b-a5c8-475c-821b-06324c4610a1.mock.pstmn.io/friendrequests/" + LoginActivity.loginID;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Handle JSON response for friend requests data
                        friendRequestList = parseFriendsRequests(response);

                        // Populate ListView with friend requests data
                        ListFriendRequests adapter = new ListFriendRequests(FriendFeatureActivity.this, friendRequestList, FriendFeatureActivity.this);
                        listViewFriendRequests.setAdapter(adapter);



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Toast.makeText(FriendFeatureActivity.this, "Error fetching friend requests data", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonArrayRequest);
    }

//
    private List<Friend> parseFriendsRequests(JSONArray jsonArray) {
        List<Friend> friendRequests = new ArrayList<>();

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
                    Friend friendRequest = new Friend(firstName, lastName, id, username);

                    friendRequests.add(friendRequest);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return friendRequests;
    }
    //PARSING CORRECTLY
    private List<Friend> parseFriendsJson(JSONArray jsonArray) {
        List<Friend> friends = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String first = jsonObject.getString("firstName");
                String last = jsonObject.getString("lastName");
                int uid = jsonObject.getInt("id");
                String username = jsonObject.getString("userName");



                Log.d("JSONParsing", "First Name: " + first + ", Last Name: " + last + ", UID: " + uid + ", Username: " + username);



                Friend friend = new Friend(first, last, uid, username);
                friends.add(friend);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return friends;
    }

    //THIS IS WORKING
    private void fetchOtherUsers() {
        String otherUsers =  URL + "/friendrequests/" + LoginActivity.loginID+ "/potential-friends";
//                String friends_url = "https://37668f7b-a5c8-475c-821b-06324c4610a1.mock.pstmn.io/friendrequests/userName123/potential-friends";


        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, otherUsers, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Handle JSON response for friend requests data
                        generalUsers = parseFriendsJson(response);

                        // Populate ListView with friend requests data
                        ListOtherUsers adapter = new ListOtherUsers(FriendFeatureActivity.this, generalUsers, FriendFeatureActivity.this);
                        listViewGenUsers.setAdapter(adapter);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Toast.makeText(FriendFeatureActivity.this, "Error fetching friend requests data", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonArrayRequest);
    }


    @Override
    public void onFriendAccepted() {

    }


    //no usages but i think this might have something to do w it
    @Override
    public void onFriendAccepted(String requesterUsername) {
        String otherUsers = URL + "/friendrequests/" + LoginActivity.loginID +"/accept/" + requesterUsername;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, otherUsers, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Handle JSON response for friend requests data
                        generalUsers = parseFriendsJson(response);

                        // Populate ListView with friend requests data
//                        ListOtherUsers adapter = new ListOtherUsers(FriendFeatureActivity.this, generalUsers, FriendFeatureActivity.this);
//                        listViewGenUsers.setAdapter(adapter);
                        friendList.addAll(generalUsers);

                        // Notify the adapter that the data set has changed
//                        ((ListFriends) listViewFriends.getAdapter()).notifyDataSetChanged();
                        ((ListFriends) listViewFriends.getAdapter()).updateFriendList(friendList);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Toast.makeText(FriendFeatureActivity.this, "Error fetching friend requests data", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonArrayRequest);

    }

    public void showModalBottomSheet(Friend friend) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                FriendFeatureActivity.this, com.google.android.material.R.style.Base_Theme_Material3_Light_BottomSheetDialog);
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.modal_bottom_sheet, null);

        // Set friend's details in the bottom sheet view
        TextView nameTextView = bottomSheetView.findViewById(R.id.friend_name);
        nameTextView.setText(friend.getFirstName() + " " + friend.getLastName());

        // Find the button within the bottom sheet view
        Button message = bottomSheetView.findViewById(R.id.sendMessageButton);
        Button unfriend = bottomSheetView.findViewById(R.id.unfriendButton);
        // Set onClickListener for the button
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click event
            }
        });

        unfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFriend(friend.getId());
                bottomSheetDialog.dismiss(); // Close the BottomSheetDialog
            }
        });

        // You can set other details similarly...

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    //IDK if this being a string request is right
    private void deleteFriend(int userID) {
//        Log.d("DeleteFriend", "Deleting friend with userID: " + userID);
        // Construct the URL for the DELETE request
        String url = URL + "/friends/" + LoginActivity.loginID +"/del/";

        //correct mapping
//        String url = url+ "/friends/"+ LoginActivity.username+"/del/" +  + userID;

        // Create the DELETE request
        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle successful response
//                        Log.d("DeleteFriend", "Friend deleted successfully");
                                Log.d("message", "Deleting friend with userID: " + userID);

                        // You can perform any further actions here after the request is successful
                        for (int i = 0; i < friendList.size(); i++) {
                            if (friendList.get(i).getId() == userID) {
                                friendList.remove(i);
                                break;
                            }
                        }

                        // Notify the adapter that the data set has changed
                        ((ListFriends) listViewFriends.getAdapter()).notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.e("DeleteFriend", "Error deleting friend: " + error.toString());
                        // You can show an error message to the user or perform any other error handling
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(request);

    }

}