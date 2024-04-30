package com.example.androidexample.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidexample.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecentlyViewed#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecentlyViewed extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recently_viewed, container, false);
    }
}