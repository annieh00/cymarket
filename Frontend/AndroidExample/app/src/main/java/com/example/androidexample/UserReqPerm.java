package com.example.androidexample;

public class UserReqPerm {
    private String username;

    private int userId;

    public UserReqPerm(int id, String userName) {
        this.userId = id;
        this.username = userName;
    }



    public String getUsername() {
        return username;
    }


    public int getId(){return userId;}
}
