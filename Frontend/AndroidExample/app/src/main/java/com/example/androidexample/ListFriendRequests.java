
package com.example.androidexample;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;


import java.util.List;

public class ListFriendRequests extends ArrayAdapter<Friend> {

    private Context context;
    private List<Friend> friendList;

    public ListFriendRequests(Context context, List<Friend> friendList) {
        super(context, 0, friendList);
        this.context = context;
        this.friendList = friendList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.list_item_friend_requests, parent, false);
        }

        Friend currentFriend = friendList.get(position);

        TextView nameTextView = listItemView.findViewById(R.id.name_text_view);
        nameTextView.setText(currentFriend.getFirstName() + " " + currentFriend.getLastName());
        nameTextView.setTextSize(30); // Set text size
        nameTextView.setTypeface(null, Typeface.NORMAL); // Set text style to bold


        // Accept Button
        ImageButton acceptButton = listItemView.findViewById(R.id.accept_button);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle accept button click here
                // You can implement your logic here
                Log.d("FriendRequests", "Accept button clicked for friend: " + currentFriend.getFirstName() + " " + currentFriend.getLastName());

            }
        });

        // Decline Button
        ImageButton declineButton = listItemView.findViewById(R.id.decline_button);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle decline button click here
                // You can implement your logic here
                Log.d("FriendRequests", "Decline button clicked for friend: " + currentFriend.getFirstName() + " " + currentFriend.getLastName());

            }
        });

        return listItemView;
    }
}