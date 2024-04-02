package com.example.androidexample.Post;

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
    Uri picture1;
    Uri picture2;
    Uri picture3;
    Uri picture4;
    Uri picture5;
    Uri picture6;
    String title;
    int price;
    String date;
    String category;
    int flagCount;

    Boolean auction;
    String description;
    int userID;
    int postID;
    public PostItemObject(Uri picture1, Uri picture2, Uri picture3, Uri picture4, Uri picture5, Uri picture6, String title, int price, String date, String category, Boolean auction, int flagCount, String description, int userID, int postID) {
        this.picture1 = picture1;
        this.picture2 = picture2;
        this.picture3 = picture3;
        this.picture4 = picture4;
        this.picture5 = picture5;
        this.picture6 = picture6;
        this.title = title;
        this.price = price;
        this.date = date;
        this.category = category;
        this.auction = auction;
        this.description = description;
        this.userID = userID;
        this.postID = postID;
        this.flagCount = flagCount;


    }

    public Uri getPicture1(){ return picture1; }
    public Uri getPicture2(){ return picture2; }
    public Uri getPicture3(){ return picture3; }
    public Uri getPicture4(){ return picture4; }
    public Uri getPicture5(){ return picture5; }
    public Uri getPicture6(){ return picture6; }

    public String getTitle() {
        return title;
    }

    public int getPrice(){ return price; }

    public String getDate(){ return date; }
    public String getCategory(){ return category; }
    public Boolean getAuction(){ return auction; }
    public String getDescription(){ return description; }
    public int getPostID() { return postID; }
    public int getUserID() { return userID; }
    public int getFlagCount(){ return flagCount; }

}

