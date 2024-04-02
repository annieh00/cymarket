package com.example.androidexample;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import android.widget.Button;

public class FriendFeatureActivity extends AppCompatActivity implements FriendAcceptedListener {

    private Toolbar toolbar;
    private ListView listViewFriends;
    private ListView listViewFriendRequests;
    private List<Friend> friendList;
    private List<Friend> friendRequestList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_feature);

        toolbar = findViewById(R.id.vwebtoolbar1);
        listViewFriends = findViewById(R.id.FriendList);
        listViewFriendRequests = findViewById(R.id.FriendRequestList);

//        FloatingActionButton OpenBottomSheet = findViewById(R.id.open_modal_bottom_sheet);
//
//        OpenBottomSheet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
//                        FriendFeatureActivity.this, com.google.android.material.R.style.Base_Theme_Material3_Light_BottomSheetDialog);
//                View bottomSheetView = LayoutInflater.from(getApplicationContext())
//                        .inflate(R.layout.modal_bottom_sheet, null);
//
//                bottomSheetDialog.setContentView(bottomSheetView);
//                bottomSheetDialog.show();
//            }
//        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        friendList = new ArrayList<>();
        friendRequestList = new ArrayList<>();

        // Call methods to make network requests
        fetchFriendsData();
        fetchFriendRequestsData();
    }

    private void fetchFriendsData() {
        String url = "https://37668f7b-a5c8-475c-821b-06324c4610a1.mock.pstmn.io/friends";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
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


    private void fetchFriendRequestsData() {
        String url = "https://37668f7b-a5c8-475c-821b-06324c4610a1.mock.pstmn.io/freindrequests";        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Handle JSON response for friend requests data
                        friendRequestList = parseFriendsJson(response);

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

    private List<Friend> parseFriendsJson(JSONArray jsonArray) {
        List<Friend> friends = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String first = jsonObject.getString("firstName");
                String last = jsonObject.getString("lastName");
                int uid = jsonObject.getInt("uid");


                Friend friend = new Friend(first, last, uid);
                friends.add(friend);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return friends;
    }

    @Override
    public void onFriendAccepted() {
        // Update the ListFriends adapter when a friend is accepted
        ListFriends adapter = (ListFriends) listViewFriends.getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

    }

    public void showModalBottomSheet(Friend friend) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                FriendFeatureActivity.this, com.google.android.material.R.style.Base_Theme_Material3_Light_BottomSheetDialog);
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.modal_bottom_sheet, null);

        // Set friend's details in the bottom sheet view
        TextView nameTextView = bottomSheetView.findViewById(R.id.friend_name);
        nameTextView.setText(friend.getFirstName() + " " + friend.getLastName());

        // You can set other details similarly...

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

//    private List<FriendRequest> parseFriendRequestsJson(JSONArray jsonArray)
}