package com.example.androidexample;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class AnnouncementAdapter extends ArrayAdapter<Announcement> {

    private Context context;
    private List<Announcement> announcements;

    public AnnouncementAdapter(Context context, List<Announcement> announcements) {
        super(context, 0, announcements);
        this.context = context;
        this.announcements = announcements;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_announcements, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.titleTextView = convertView.findViewById(R.id.AnnouncementsTitle);
            viewHolder.descriptionTextView = convertView.findViewById(R.id.AnnouncementsDescription);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Announcement announcement = announcements.get(position);

        viewHolder.titleTextView.setText(announcement.getTitle());
        viewHolder.descriptionTextView.setText(announcement.getDescription());

        ImageButton edit = convertView.findViewById(R.id.editAnnouncement);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                update
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
    }
}
