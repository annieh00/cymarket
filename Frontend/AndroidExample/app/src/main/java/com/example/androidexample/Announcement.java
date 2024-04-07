package com.example.androidexample;
public class Announcement {

    private String title;
    private String description;

    private int ID;




    public Announcement(String title, String description, int announcementID) {
//        this.id = id;
        this.title = title;
        this.description = description;
        this.ID = announcementID;
//        this.ID = announcementID;
    }

    public String getTitle(){return title;}

    public String getDescription(){return description;}

    public int getID(){return ID;}


}