package com.example.androidexample;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class PostAdapter<T> extends ArrayAdapter<T> {

    private static int LIST;

    public PostAdapter(Context context, List<T> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        T item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.post_item, parent, false);
        }

        // Lookup view for data population
        TextView itemName = convertView.findViewById(R.id.itemTitle);
        ImageView picture = convertView.findViewById(R.id.imageSelView1);
        TextView price = convertView.findViewById(R.id.itemPrice);
//        TextView itemUsername = convertView.findViewById(R.id.itemUsername);

        // Populate the data into the template view using the data object
        PostItemObject items = (PostItemObject) item;

        itemName.setText(items.getTitle());
        picture.setImageURI(items.getPicture());
        price.setText(items.getPrice());

        // Return the completed view to render on screen
        return convertView;
    }


}

