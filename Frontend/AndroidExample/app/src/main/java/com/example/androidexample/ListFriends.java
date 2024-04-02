
package com.example.androidexample;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;


import com.android.volley.Response;

import org.json.JSONArray;

import java.util.List;

public class ListFriends extends ArrayAdapter<Friend> implements FriendAcceptedListener{

    private Context context;
    public static List<Friend> friendList;

    private FriendAcceptedListener listener; // Interface reference

    public ListFriends(Context context, List<Friend> friendList) {
        super(context, 0, friendList);
        this.context = context;
        this.friendList = friendList;
//        this.listener = listener;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.list_item_friend, parent, false);
        }

        Friend currentFriend = friendList.get(position);

        TextView nameTextView = listItemView.findViewById(R.id.name_text_view);
        nameTextView.setText(currentFriend.getFirstName() + " " + currentFriend.getLastName());
        nameTextView.setTextSize(30); // Set text size
        nameTextView.setTypeface(null, Typeface.NORMAL); // Set text style to bold

        ImageButton moreInfo = listItemView.findViewById(R.id.moreInfo);
        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the interface method when moreInfo button is clicked
                if (listener != null) {
                    listener.onFriendAccepted();
                }
            }
        });



        return listItemView;
    }

    @Override
    public void onFriendAccepted() {
        // Update the adapter when a friend is accepted
        notifyDataSetChanged();

    }
}