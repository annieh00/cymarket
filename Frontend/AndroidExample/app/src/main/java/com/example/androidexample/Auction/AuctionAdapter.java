package com.example.androidexample.Auction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidexample.R;
import java.util.List;

public class AuctionAdapter extends RecyclerView.Adapter<AuctionAdapter.ViewHolder> {

    private List<AuctionItemObject> mAuctionList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(AuctionItemObject post);
    }

    public AuctionAdapter(List<AuctionItemObject> mPostList, OnItemClickListener listener){
        this.mAuctionList = mPostList;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.auction_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mAuctionList.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mAuctionList.size();
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

        public void bind(final AuctionItemObject item, final OnItemClickListener listener) {
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