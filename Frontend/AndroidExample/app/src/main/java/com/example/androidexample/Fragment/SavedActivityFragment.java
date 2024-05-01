package com.example.androidexample.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.LoginActivity;
import com.example.androidexample.Post.PostAdapter;
import com.example.androidexample.Post.PostItemObject;
import com.example.androidexample.R;
import com.example.androidexample.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SavedActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedActivityFragment extends Fragment {

    public PostAdapter mSavedPostAdapter;

    public RecyclerView mRecyclerViewSaved;

    public static final String DOMAIN = "http://coms-309-060.class.las.iastate.edu:8080";



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

        getBookmarks();
        return rootView;
    }

    private void getBookmarks() {
        String url = DOMAIN + "/bookmarks/" + LoginActivity.loginID;

        // Create a JSON array request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Clear the existing list before adding new items
                            mSavedList.clear();

                            // Iterate through the JSON array
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject bookmarkObj = response.getJSONObject(i);

//
                                String picture1 = bookmarkObj.getString("picture1");
                                String picture2 = bookmarkObj.getString("picture2");
                                String picture3 = bookmarkObj.getString("picture3");
                                String picture4 = bookmarkObj.getString("picture4");
                                String picture5 = bookmarkObj.getString("picture5");
                                String picture6 = bookmarkObj.getString("picture6");
                                String title = bookmarkObj.getString("title");
                                int price = bookmarkObj.getInt("price");
                                Boolean auction = bookmarkObj.getBoolean("isAuction");
                                String description = bookmarkObj.getString("description");
                                String userName = bookmarkObj.getString("userName");
                                int id = bookmarkObj.getInt("id");

                                // Create a new PostItemObject instance
                                PostItemObject postItem = new PostItemObject(picture1, picture2, picture3, picture4, picture5, picture6, title, price, auction, description, userName, id);

                                // Add the new PostItemObject instance to the list
                                mSavedList.add(postItem);
                            }

                            // Notify the adapter about the data change
                            mSavedPostAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.e("Volley Error", error.toString());
                        // You can show an error message to the user or perform any other error handling
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(requireContext()).add(jsonArrayRequest);
    }
}