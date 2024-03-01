package com.example.androidexample;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class ListAdapter extends ArrayAdapter<ListItemObject> {

    public ListAdapter(ViewPostActivity context, List<ListItemObject> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ListItemObject item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Lookup view for data population
        TextView itemName = convertView.findViewById(R.id.itemName);
        TextView itemEmail = convertView.findViewById(R.id.itemEmail);
        TextView itemUsername = convertView.findViewById(R.id.itemUsername);

        // Populate the data into the template view using the data object
        itemName.setText(item.getName());
        itemEmail.setText(item.getEmail());

        // Return the completed view to render on screen
        return convertView;
    }
}

