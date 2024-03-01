package com.example.androidexample;

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
