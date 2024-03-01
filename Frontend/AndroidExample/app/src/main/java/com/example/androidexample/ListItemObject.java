package com.example.androidexample;

public class ListItemObject {
    private String name;
    private String email;
private String username;
    public ListItemObject(String name, String email, String username) {
        this.name = name;
        this.email = email;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
    public String getUsername(){ return username;
    }
}
