package com.example.androidexample.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidexample.Post.PostAdapter;
import com.example.androidexample.Post.PostItemObject;
import com.example.androidexample.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SavedActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedActivityFragment extends Fragment {

    public PostAdapter mSavedPostAdapter;

    public RecyclerView mRecyclerViewSaved;


    public static ArrayList<PostItemObject> mSavedList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_saved_activity, container, false);
        mRecyclerViewSaved = rootView.findViewById(R.id.recycler_view_saved);
        LinearLayoutManager linearManager = new LinearLayoutManager(getContext());
        mRecyclerViewSaved.setLayoutManager(linearManager);

//        // Initialize mSavedList if it's null
//        if (mSavedList == null) {
//            mSavedList = new ArrayList<>();
//        }

        // Initialize the adapter with your list and set it to the RecyclerView
        mSavedPostAdapter = new PostAdapter(mSavedList, new PostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PostItemObject post) {
                // Handle item click if needed
            }
        });
        mRecyclerViewSaved.setAdapter(mSavedPostAdapter);

        return rootView;
    }
}