package com.example.androidexample;
public class Announcement {
    private int id;
    private String title;
    private String description;


    public Announcement(String title, String description) {
//        this.id = id;
        this.title = title;
        this.description = description;
    }

    public String getTitle(){return title;}

    public String getDescription(){return description;}




    // Getter methods for id, title, and description
}