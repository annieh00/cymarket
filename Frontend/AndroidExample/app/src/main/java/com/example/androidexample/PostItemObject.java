package com.example.androidexample;

import android.icu.text.CaseMap;
import android.net.Uri;

//public class ListItemObject {
//    private String x;
//    private String y;
//
//    private String title;
//    private String description;
//
//    //we should come up with different names
////    public ListItemObject(String name, String email) {
////        this.name = name;
////        this.email = email;
////    }
//
//
//    public ListItemObject(String title, String description) {
//        this.title = title;
//        this.description = description;
//    }
//
//
//    public ListItemObjectCoords(String x, String y) {
//        this.x = x;
//        this.y = y;
//    }
//
////    public String getName() {
////        return name;
////    }
////
////    public String getEmail() {
////        return email;
////    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//
//    public String getX() {
//        return x;
//    }
//
//    public String getY() {
//        return y;
//    }
//
//
//
//
//}

public class PostItemObject {
    private Uri picture;
    private String title;
    private int price;
    public PostItemObject(Uri picture, String title, int price) {
        this.title = title;
        this.picture= picture;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public Uri getPicture() {
        return picture;
    }

    public int getPrice(){ return price; }
}

