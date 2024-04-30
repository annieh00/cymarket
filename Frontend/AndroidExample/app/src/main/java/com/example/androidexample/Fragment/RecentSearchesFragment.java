package com.example.androidexample.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.LoginActivity;
import com.example.androidexample.R;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecentSearchesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecentSearchesFragment extends Fragment {

    TextView clearAllSearches;

    RequestQueue requestQueue;

    ListView recentSearchesListView;
    ArrayAdapter<String> searchResultAdapter;


    private String URL = "http://coms-309-060.class.las.iastate.edu:8080";

    private ListView listView;
    private ArrayAdapter<String> adapter;
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recent_searches, container, false);

        // Initialize components
        clearAllSearches = rootView.findViewById(R.id.clearAllTextView);

        requestQueue = Volley.newRequestQueue(requireContext());

        listView = rootView.findViewById(R.id.listViewRecentSearches);
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);


        // Set listeners or perform any other setup
        clearAllSearches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSearches();
            }

        });

        return rootView;


    }

    // Method to update the ListView with recent searches data
    public void updateRecentSearches(List<String> recentSearches) {
        if (getContext() != null) {
            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, recentSearches);
            listView.setAdapter(adapter); // Update this line to use listView
        }
    }

    private void clearSearches() {

        // Make network request
        String url = URL + "/search/" + LoginActivity.loginID + "/clear";

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Clear data from the adapter
                        adapter.clear();
                        // Notify the ListView to update its display
                        adapter.notifyDataSetChanged();

                        Log.d("Delete Search", response.toString());



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Did not work", error.toString());
                    }
                });

        // Add the request to the RequestQueue

        Volley.newRequestQueue(requireContext()).add(request);




    }

}