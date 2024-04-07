package com.example.androidexample;
import android.content.Context;
import android.content.Intent;
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

    private String URL = "http://coms-309-060.class.las.iastate.edu:8080";


    public AnnouncementAdapter(Context context, List<Announcement> announcements) {
        super(context, 0, announcements);
        this.context = context;
        this.announcements = announcements;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {

            if(LoginActivity.permission == 0) {
                convertView = LayoutInflater.from(context).inflate(R.layout.list_announcements, parent, false);

            }
            if (LoginActivity.permission == 2){
                convertView = LayoutInflater.from(context).inflate(R.layout.list_announcements_user, parent, false);


            }


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
        if(LoginActivity.permission == 0) {

            ImageButton edit = convertView.findViewById(R.id.editAnnouncement);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("edit_announcement");
                    context.sendBroadcast(intent);

                    if (context instanceof ViewAnnouncementAdmin) {
                        ((ViewAnnouncementAdmin) context).showModalBottomSheet(announcement);

                    }


                }
            });
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
    }
}
