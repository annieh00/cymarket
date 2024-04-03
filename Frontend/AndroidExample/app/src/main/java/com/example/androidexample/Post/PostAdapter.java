package com.example.androidexample.Post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidexample.R;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    /**
     * The context of the activity that is using the adapter.
     */
    private Context mContext;
    /**
     * The list of posts that will be displayed.
     */
    private List<PostItem> mPostList;

    /**
     * An interface that will be called when a poll is clicked.
     */

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }


    /**
     * Sets the listener that will be called when an item in a RecyclerView is clicked.
     *
     * @param listener The listener that will be called when an item in a RecyclerView is clicked.
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public PostAdapter(Context context, List<PostItem> mPostList){
        mContext = context;
        this.mPostList = mPostList;
    }

    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.title.setText("Title: " + mPostList.get(position).getTitle());
        holder.image.setImageURI(mPostList.get(position).getPicture1());
        holder.price.setText("Price: " + String.valueOf(mPostList.get(position).getPrice()));    }

    @Override
    public int getItemCount() {return mPostList.size();}

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView price, title;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageSelView1);
            price = itemView.findViewById(R.id.itemPrice);
            title = itemView.findViewById(R.id.itemTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                });
            }

        }


    }