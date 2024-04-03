package com.example.androidexample;

import static com.example.androidexample.MainFeed.EXTRA_postAuction;
import static com.example.androidexample.MainFeed.EXTRA_postCategory;
import static com.example.androidexample.MainFeed.EXTRA_postDate;
import static com.example.androidexample.MainFeed.EXTRA_postID;
import static com.example.androidexample.MainFeed.EXTRA_postPicture1;
import static com.example.androidexample.MainFeed.EXTRA_postPicture2;
import static com.example.androidexample.MainFeed.EXTRA_postPicture3;
import static com.example.androidexample.MainFeed.EXTRA_postPicture4;
import static com.example.androidexample.MainFeed.EXTRA_postPicture5;
import static com.example.androidexample.MainFeed.EXTRA_postPicture6;
import static com.example.androidexample.MainFeed.EXTRA_postAuction;
import static com.example.androidexample.MainFeed.EXTRA_postAuction;
import static com.example.androidexample.MainFeed.EXTRA_postAuction;
import static com.example.androidexample.MainFeed.EXTRA_postAuction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class PostDetails extends AppCompatActivity {
    private ViewPager viewPager;
    private ImageAdapter imageAdapter;
//    private int[] imageResources = {R.id.imageSelView1, R.id.imageSelView2, R.id.imageSelView3, R.id.imageSelView4, R.id.imageSelView5, R.id.imageSelView6}; // Add your image resources here
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_item);

//        viewPager = findViewById(R.id.viewPager);
//        imageAdapter = new ImageAdapter(this, imageResources);
//        viewPager.setAdapter(imageAdapter);

//        ImageView previousArrow = findViewById(R.id.previousArrow);
//        ImageView nextArrow = findViewById(R.id.nextArrow);

//        previousArrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (currentPosition > 0) {
//                    currentPosition--;
//                } else {
//                    currentPosition = imageResources.length - 1;
//                }
//                viewPager.setCurrentItem(currentPosition, true);
//            }
//        });
//
//        nextArrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (currentPosition < imageResources.length - 1) {
//                    currentPosition++;
//                } else {
//                    currentPosition = 0;
//                }
//                viewPager.setCurrentItem(currentPosition, true);
//            }
//        });

    }

}
