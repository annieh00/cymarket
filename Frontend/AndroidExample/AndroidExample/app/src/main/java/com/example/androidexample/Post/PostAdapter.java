package com.example.androidexample.Post;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidexample.R;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<PostItemObject> mPostList;
    private List<AuctionItemObject> mAuctionList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(PostItemObject post);
    }

    public PostAdapter(List<PostItemObject> mPostList, OnItemClickListener listener){
        this.mPostList = mPostList;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mPostList.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView price, title;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageSelView1);
            price = itemView.findViewById(R.id.itemPrice);
            title = itemView.findViewById(R.id.itemTitle);
        }

        public void bind(final PostItemObject item, final OnItemClickListener listener) {
            title.setText(item.getTitle());
            price.setText(String.valueOf(item.getPrice()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}