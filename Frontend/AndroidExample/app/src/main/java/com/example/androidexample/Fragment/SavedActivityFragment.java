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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.Const;
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

        mSavedList.clear();
        // Create a String request
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("bookmarks");
                        for (int i = jsonArray.length() - 1; i >= 0; i--) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String picture1 = jsonObject.getString("picture1");
                            String picture2 = jsonObject.getString("picture2");
                            String picture3 = jsonObject.getString("picture3");
                            String picture4 = jsonObject.getString("picture4");
                            String picture5 = jsonObject.getString("picture5");
                            String picture6 = jsonObject.getString("picture6");
                            String title = jsonObject.getString("title");
                            int price = jsonObject.getInt("price");
                            Boolean auction = jsonObject.getBoolean("isAuction");
                            String description = jsonObject.getString("description");
                            String userName = jsonObject.getString("userName");
                            int id = jsonObject.getInt("id");
                            String pic1Data = jsonObject.getString("picture1Data");
                            mSavedList.add(new PostItemObject(pic1Data, picture1, picture2, picture3, picture4, picture5, picture6, title, price, auction, description, userName, id));
                        }


                        mRecyclerViewSaved.setAdapter(mSavedPostAdapter);
                        mSavedPostAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
            // Handle error
        });
        VolleySingleton.getInstance(requireContext()).addToRequestQueue(jsonArrayRequest);
    }
}
