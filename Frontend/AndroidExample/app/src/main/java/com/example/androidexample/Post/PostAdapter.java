//package com.example.androidexample;
//
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import org.w3c.dom.Text;
//
//import java.util.List;
//
//public class PostAdapter<T> extends ArrayAdapter<T> {
//
//    private static int LIST;
//
//    public PostAdapter(Context context, List<T> items) {
//        super(context, 0, items);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // Get the data item for this position
//        T item = getItem(position);
//
//        // Check if an existing view is being reused, otherwise inflate the view
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.post_item, parent, false);
//        }
//
//        // Lookup view for data population
//        TextView itemName = convertView.findViewById(R.id.itemTitle);
//        ImageView picture = convertView.findViewById(R.id.imageSelView1);
//        TextView price = convertView.findViewById(R.id.itemPrice);
////        TextView itemUsername = convertView.findViewById(R.id.itemUsername);
//
//        // Populate the data into the template view using the data object
//        PostItemObject items = (PostItemObject) item;
//
//        itemName.setText(items.getTitle());
//        picture.setImageURI(items.getPicture());
//        price.setText(items.getPrice());
//
//        // Return the completed view to render on screen
//        return convertView;
//    }
//
//
//}
//

package com.example.androidexample.Post;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidexample.R;

import java.util.List;

/**
 * A class that displays a list of posts in RecyclerView.
 */

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {


    /**
     * The context of the activity that is using the adapter.
     */
    private Context mContext;

    /**
     * The list of posts that will be displayed.
     */
    private List<PostItemObject> mPostList;

    /**
     * An interface that will be called when a post is clicked.
     */
    private OnItemClickListener mListener;

    /**
     * An interface that will be called when an item in a RecyclerView is clicked.
     *
     */
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
    /**
     * Constructs a new PollAdapter with the given context and list of polls.
     *
     * @param context The context of the activity that is using the adapter.
     * @param mPostList The list of polls that will be displayed.
     */
    public PostAdapter(Context context, List<PostItemObject> mPostList){
        mContext = context;
        this.mPostList = mPostList;

    }
    /**
     * Creates a new ViewHolder for the given viewType.
     *
     * @param parent The ViewGroup that will contain the ViewHolder.
     * @param viewType The type of the ViewHolder.
     * @return A new ViewHolder for the given viewType.
     */

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(v);
    }



    /**
     * Binds the data for the given position to the ViewHolder.
     *
     * @param holder The ViewHolder that will be bound with the data.
     * @param position The position of the data in the list of posts.
     */
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.price.setText(String.valueOf(mPostList.get(position).getPrice()));
        holder.title.setText(mPostList.get(position).getTitle());
//        holder.image.setImageURI(mPostList.get(position).getPicture());

    }

    /**
     * Returns the number of posts in the list.
     * @return number of posts in the list
     */
    @Override
    public int getItemCount(){
        return mPostList.size();
    }
}



