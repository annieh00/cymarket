package com.example.androidexample.Donation;

import android.net.Uri;

public class DonationItemObject {
    String picture1;
    String picture2;
    String picture3;
    String picture4;
    String picture5;
    String picture6;
    String title;
    int price;
    String date;
    String category;
    int flagCount;

    Boolean auction;
    String description;
    String userName;
    int postID;
    public DonationItemObject(String picture1, String picture2, String picture3, String picture4, String picture5, String picture6, String title, int price, Boolean auction, String description, String userName, int postID) {
        this.picture1 = picture1;
        this.picture2 = picture2;
        this.picture3 = picture3;
        this.picture4 = picture4;
        this.picture5 = picture5;
        this.picture6 = picture6;
        this.title = title;
//        this.price = price;
//        this.date = date;
//        this.category = category;
//        this.auction = auction;
        this.description = description;
        this.userName = userName;
        this.postID = postID;
//        this.flagCount = flagCount;


    }

    public String getPicture1(){ return picture1; }
    public String getPicture2(){ return picture2; }
    public String getPicture3(){ return picture3; }
    public String getPicture4(){ return picture4; }
    public String getPicture5(){ return picture5; }
    public String getPicture6(){ return picture6; }

    public String getTitle() {
        return title;
    }

//    public int getPrice(){ return price; }

    public String getDate(){ return date; }
    public String getCategory(){ return category; }
//    public Boolean getAuction(){ return auction; }
    public String getDescription(){ return description; }
    public int getPostID() { return postID; }
    public String getUserName() { return userName; }
    public int getFlagCount(){ return flagCount; }

}

