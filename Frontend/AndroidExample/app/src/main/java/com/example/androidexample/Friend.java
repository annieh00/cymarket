package com.example.androidexample;

public class Friend {
    private String firstName;
    private String lastName;

    private int userId;

    public Friend(String first, String last, int id) {
        this.firstName = first;
        this.lastName = last;
        this.userId = id;
    }


    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }

    public int getId(){return userId;}
}
