package com.example.androidexample;

import android.icu.text.CaseMap;

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

public class ListItemObject {
    private String title;
    private String description;
    private int userName;
    public ListItemObject(String name, String email, int username) {
        this.title = name;
        this.description= email;
        this.userName = username;
    }

    public String getName() {
        return title;
    }

    public String getEmail() {
        return description;
    }
    public int getUsername(){ return userName;
    }
}

