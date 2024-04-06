
package com.example.androidexample;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.List;

public class ListOtherUsers extends ArrayAdapter<Friend> implements FriendAcceptedListener{

    private Context context;
    private List<Friend> genUsers;
    private FriendAcceptedListener listener;
    public ListOtherUsers(Context context, List<Friend> generalList, FriendAcceptedListener listener) {
        super(context, 0, generalList);
        this.context = context;
        this.genUsers = generalList;
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.list_other_users, parent, false);
        }

        Friend currentFriend = genUsers.get(position);

        TextView nameTextView = listItemView.findViewById(R.id.name_text_view);
        nameTextView.setText(currentFriend.getFirstName() + " " + currentFriend.getLastName());
        nameTextView.setTextSize(30); // Set text size
        nameTextView.setTypeface(null, Typeface.NORMAL); // Set text style to bold

        //i am accepting friend so what kind of request would this be if i am updating the friend list?
        //i think update, i wonder what all i need to do that?
        // Accept Button
        ImageButton addButton = listItemView.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent("friend_requested");
                context.sendBroadcast(intent);

                // Assuming currentFriend has a first name & last
                String newFirst = currentFriend.getFirstName();
                String  newLast = currentFriend.getLastName();
                int uid = currentFriend.getId();
                String username = currentFriend.getUsername();
                Log.d("UsernameTest", "Username:" + username);

                Friend friend = new Friend(newFirst, newLast, uid, username);

                sendFriendRequest(friend);

                addButton.setImageResource(R.drawable.pending);


            }
        });

        return listItemView;
    }



    //THIS IS WORKING
    private void sendFriendRequest(Friend sentTo) {
        String url = "http://coms-309-060.class.las.iastate.edu:8443" +  "/friendrequests/" + LoginActivity.loginID + "/send/" + sentTo.getId();

        // Create a StringRequest with POST method
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle successful response
//                        Log.d("FriendRequest", "Friend request sent successfully");
                        // You can perform any additional actions upon successful request here
                        Log.d("FriendRequest", "Friend request sent successfully to user ID: " + sentTo.getId());
                        Log.d("FriendRequest", "Friend request sent successfully to user ID: " + LoginActivity.loginID);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    //this is the issues
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Log.e("FriendRequest", "Error sending friend request: " + error.getMessage());
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(context).add(stringRequest);
    }

    @Override
    public void onFriendAccepted() {}

    @Override
    public void onFriendAccepted(String requesterUsername) {}
}