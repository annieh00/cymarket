package com.example.androidexample;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.androidexample.Fragment.RecentSearchesFragment;
import com.example.androidexample.Fragment.SavedActivityFragment;

public class SearchActivityTabAdapter extends FragmentStateAdapter {
        public SearchActivityTabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new RecentSearchesFragment();

            case 1:
                return new SavedActivityFragment();
            default:
                return new RecentSearchesFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
