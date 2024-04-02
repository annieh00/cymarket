
package com.example.androidexample;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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

public class ListFriendRequests extends ArrayAdapter<Friend> implements FriendAcceptedListener{

    private Context context;
    private List<Friend> pendingRequests;

    private FriendAcceptedListener listener;

    public ListFriendRequests(Context context, List<Friend> friendList, FriendAcceptedListener listener) {
        super(context, 0, friendList);
        this.context = context;
        this.pendingRequests = friendList;
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.list_item_friend_requests, parent, false);
        }

        Friend currentFriend = pendingRequests.get(position);

        TextView nameTextView = listItemView.findViewById(R.id.name_text_view);
        nameTextView.setText(currentFriend.getFirstName() + " " + currentFriend.getLastName());
        nameTextView.setTextSize(30); // Set text size
        nameTextView.setTypeface(null, Typeface.NORMAL); // Set text style to bold


        //i am accepting friend so what kind of request would this be if i am updating the friend list?
        //i think update, i wonder what all i need to do that?
        // Accept Button
        ImageButton acceptButton = listItemView.findViewById(R.id.accept_button);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent("friend_accepted");
                context.sendBroadcast(intent);

                // Assuming currentFriend has a first name & last
                String newFirst = currentFriend.getFirstName();
                String  newLast = currentFriend.getLastName();
                int uid = currentFriend.getId();

                Friend acceptedFriend = new Friend(newFirst, newLast, uid);
                ListFriends.friendList.add(acceptedFriend);

                if (listener != null) {
                    listener.onFriendAccepted();
                }

//                acceptFriend(newFirst, newLast);

                //need logic to add the user to the friend list, two different lists
                // Add the user from the list
                pendingRequests.remove(currentFriend);
                notifyDataSetChanged(); // Notify the adapter that the dataset has changed



            }
        });

        // Decline Button
        ImageButton declineButton = listItemView.findViewById(R.id.decline_button);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle decline button click here
                // You can implement your logic here

                // Assuming currentFriend has an ID to identify the user to delete
                int friendId = currentFriend.getId();
                deleteFriend(friendId);

                // Remove the user from the list
                pendingRequests.remove(currentFriend);
                notifyDataSetChanged(); // Notify the adapter that the dataset has changed
            }
        });
        return listItemView;
    }

    private void acceptFriend(int uid) {

        String url = "https://37668f7b-a5c8-475c-821b-06324c4610a1.mock.pstmn.io/friends";
        StringRequest request = new StringRequest(Request.Method.PATCH, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Friend added successfully", Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error....", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(context).add(request);
    }

    // Method to send DELETE request
    private void deleteFriend(int friendId) {
        // Assuming you're using Volley for network requests
        String url = "https://37668f7b-a5c8-475c-821b-06324c4610a1.mock.pstmn.io/declineFriend/" + friendId;
        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Friend deleted successfully", Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error....", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(context).add(request);
    }

    @Override
    public void onFriendAccepted() {

    }
}

