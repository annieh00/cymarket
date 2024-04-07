package com.example.androidexample;

public class Friend {
    private String firstName;
    private String lastName;

    private String username;

    private int userId;

    public Friend(String first, String last, int id, String userName) {
        this.firstName = first;
        this.lastName = last;
        this.userId = id;
        this.username = userName;
    }


    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }


    public int getId(){return userId;}
}
