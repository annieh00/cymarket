package com.example.androidexample.Auction;

import static com.example.androidexample.PostDetailActivity.decodeBase64ToBitmap;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidexample.Auction.AuctionItemObject;
import com.example.androidexample.R;
import java.util.List;

public class AuctionAdapter extends RecyclerView.Adapter<AuctionAdapter.ViewHolder> {

    private List<AuctionItemObject> mPostList;
    private OnItemClickListener mListener;

    public Object getAdapter() {
        return mPostList;
    }
    public interface OnItemClickListener{
        void onItemClick(AuctionItemObject post);
    }

    public AuctionAdapter(List<AuctionItemObject> mPostList, OnItemClickListener listener){
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

        public void bind(final AuctionItemObject item, final OnItemClickListener listener) {
            title.setText(item.getTitle());
            price.setText(String.valueOf(item.getPrice()));
            Bitmap bm = decodeBase64ToBitmap(item.getPicture1Data());
            bm = Bitmap.createScaledBitmap(bm,150,150,false);
            image.setImageBitmap(bm);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}