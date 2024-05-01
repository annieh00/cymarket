
package com.example.androidexample;
import static com.example.androidexample.FriendFeatureActivity.listViewFriends;
import static com.example.androidexample.ListFriends.friendList;

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

public class ListOrganizationPermissions extends ArrayAdapter<UserReqPerm> implements ReqPermAcceptedListener{

    private Context context;
    private List<UserReqPerm> pendingRequests;

    private ReqPermAcceptedListener listener;


    private String URL = "http://coms-309-060.class.las.iastate.edu:8080";

//    private YourAdapter adapter;

    public ListOrganizationPermissions(Context context, List<UserReqPerm> friendList, ReqPermAcceptedListener listener) {
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

        UserReqPerm currentFriend = pendingRequests.get(position);

        TextView nameTextView = listItemView.findViewById(R.id.name_text_view);
        nameTextView.setText(currentFriend.getUsername());
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
//                String newFirst = currentFriend.getFirstName();
//                String  newLast = currentFriend.getLastName();
                int uid = currentFriend.getId();
                String username = currentFriend.getUsername();

                UserReqPerm acceptedFriend = new UserReqPerm(uid, username);

                acceptFriendRequest(acceptedFriend);
//                friendList.add(acceptedFriend);

                if (listener != null) {
                    listener.onOrgAccepted();
                }

                //NEED SOME post request to back end
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
                String friendUsername = currentFriend.getUsername();
                UserReqPerm delete = new UserReqPerm(currentFriend.getId(), currentFriend.getUsername());
                deleteFriend(delete);

                // Remove the user from the list
                pendingRequests.remove(currentFriend);
                notifyDataSetChanged(); // Notify the adapter that the dataset has changed
            }
        });
        return listItemView;
    }

    //THIS MAPPING IS WORKING
    private void acceptFriendRequest(UserReqPerm username) {
        String newUrl = URL + "/friendrequests/" + LoginActivity.loginID + "/accept/" +username.getId();
//        String url = "https://37668f7b-a5c8-475c-821b-06324c4610a1.mock.pstmn.io/friendrequests/userName123/accept/newfriend";

        StringRequest request = new StringRequest(Request.Method.POST, newUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle successful response
                        Log.d("AcceptFriendRequest", "Friend request from " + username + " accepted successfully");
                        // You can perform any further actions here after the request is successful
                        //update of adapter not working here
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.d("AcceptFriendRequest", "Error accepting friend request from " + username + ": " + error.toString());
                        // You can show an error message to the user or perform any other error handling
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this.getContext()).add(request);


        if (listViewFriends != null && listViewFriends.getAdapter() instanceof ListFriends) {
            ((ListFriends) listViewFriends.getAdapter()).notifyDataSetChanged();
        }

    }

    //THIS MAPPING WORKS
    private void deleteFriend(UserReqPerm id) {
        // Assuming you're using Volley for network requests
//        String url = "https://37668f7b-a5c8-475c-821b-06324c4610a1.mock.pstmn.io/friendsrequests/userName123/reject/";
        String url = URL + "/friendrequests/"+ LoginActivity.loginID+"/reject/" + id.getId();

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DeleteFriend", "Username of deleted user: " + id.getId());

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
    public void onOrgAccepted() {

    }

    @Override
    public void onOrgAccepted(String requesterUsername) {

    }
}

