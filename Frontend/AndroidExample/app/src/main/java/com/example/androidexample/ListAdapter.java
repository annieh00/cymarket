package com.example.androidexample;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class ListAdapter<T> extends ArrayAdapter<T> {

    private static int LIST;

    public ListAdapter(Context context, List<T> items) {
        super(context, 0, items);
    }





    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        T item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.announcement_list_item, parent, false);
        }

        // Lookup view for data population
        TextView itemName = convertView.findViewById(R.id.itemTitle);
        TextView itemEmail = convertView.findViewById(R.id.itemDescription);

        // Populate the data into the template view using the data object
        ListItemObject items = (ListItemObject) item;

        itemName.setText(items.getTitle());
        itemEmail.setText(items.getDescription());

        // Return the completed view to render on screen
        return convertView;
    }


}

