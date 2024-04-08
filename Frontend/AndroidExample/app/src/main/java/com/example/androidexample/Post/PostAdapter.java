package com.example.androidexample.Post;

import static com.example.androidexample.PostDetailActivity.decodeBase64ToBitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
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

        public static Bitmap decodeBase64ToBitmap(String base64Image) {
            byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        }

        public void bind(final PostItemObject item, final OnItemClickListener listener) {
            title.setText(item.getTitle());
            price.setText(String.valueOf(item.getPrice()));
            Bitmap bm = decodeBase64ToBitmap(item.getPicture1());
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