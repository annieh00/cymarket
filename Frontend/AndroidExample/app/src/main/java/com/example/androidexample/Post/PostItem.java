package com.example.androidexample.Post;

import android.net.Uri;

public class PostItem {
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
    public PostItem(Uri picture1, Uri picture2, Uri picture3, Uri picture4, Uri picture5, Uri picture6, String title, int price, String date, String category, Boolean auction, int flagCount, String description, int userID, int postID) {
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
    public void setPicture1(Uri pic1){ picture1 = pic1; }

    public Uri getPicture2(){ return picture2; }
    public void setPicture2(Uri pic2){ picture2 = pic2; }

    public Uri getPicture3(){ return picture3; }
    public void setPicture3(Uri pic3){ picture3 = pic3; }

    public Uri getPicture4(){ return picture4; }
    public void setPicture4(Uri pic4){ picture4 = pic4; }

    public Uri getPicture5(){ return picture5; }
    public void setPicture5(Uri pic5){ picture5 = pic5; }

    public Uri getPicture6(){ return picture6; }
    public void setPicture6(Uri pic6){ picture6 = pic6; }

    public String getTitle(){ return title; }
    public void setTitle(String title){ this.title = title; }

    public int getPrice(){ return price; }
    public void setPrice(int p){ price = p;}

    public String getDate(){ return date; }
    public void setDate(String d){ date = d;}
    public String getCategory(){ return category; }
    public void setCategory(String c){ category = c; }
    public Boolean getAuction(){ return auction; }
    public void setAuction(Boolean a){ auction = a;}
    public String getDescription(){ return description; }
    public void setDescription(String description) { this.description = description; }
    public int getPostID() { return postID; }
    public void setPostID(int postID){ this.postID = postID; }
    public int getUserID() { return userID; }
    public void setUserID(int uID){ this.userID = uID; }
    public int getFlagCount(){ return flagCount; }
    public void setFlagCount(int f){ flagCount = f; }

}