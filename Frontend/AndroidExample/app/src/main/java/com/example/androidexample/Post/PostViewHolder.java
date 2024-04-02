package com.example.androidexample.Post;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidexample.R;

public class PostViewHolder extends RecyclerView.ViewHolder {

    ImageView image;
    TextView price, title;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.imageSelView1);
        price = itemView.findViewById(R.id.itemPrice);
        title = itemView.findViewById(R.id.itemTitle);
    }
}
