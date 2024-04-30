package com.example.androidexample;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.androidexample.Fragment.RecentSearchesFragment;
import com.example.androidexample.Fragment.SavedActivityFragment;

import java.util.ArrayList;
import java.util.List;

public class SearchActivityTabAdapter extends FragmentStateAdapter {


    private List<Fragment> fragments = new ArrayList<>();

    public SearchActivityTabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new RecentSearchesFragment();
                break;
            case 1:
                fragment = new SavedActivityFragment();
                break;
            default:
                fragment = new RecentSearchesFragment();
                break;
        }
        fragments.add(fragment); // Add the created fragment to the list
        return fragment;
    }
    @Override
    public int getItemCount() {
        return 2;
    }


    public Fragment getFragmentAtPosition(int position) {
        return fragments.get(position);
    }
}
