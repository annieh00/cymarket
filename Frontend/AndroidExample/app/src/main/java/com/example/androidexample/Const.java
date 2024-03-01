package com.example.androidexample;

public class Const {
    //the domain of the server
    public static final String DOMAIN = "http://coms-309-060.class.las.iastate.edu:8080";
    //URL to post login user data
    public static final String URL_POST_LOGIN_USER = DOMAIN + "/login";
    //URL to post register user data
    public static final String URL_POST_REGISTER_USER = DOMAIN + "/signup";
    public static final String URL_POSTS = DOMAIN + "/posts";
    public static final String URL_GET_ALL_POSTS = DOMAIN + "/getAllPosts";
    public static final String URL_UPDATE_USER = DOMAIN + "/login" + "/editUser";
    //URL to make a post
    public static final String URL_CREATE_POST = DOMAIN + "/create_post";
    public static final String URL_GET_ALL_USERS = DOMAIN + "/login" + "/getAllUsers";

    public static final String URL_DELETE_USER = DOMAIN + "/login" + "/deleteUser";
    public static final String URL_DELETE_POST = DOMAIN + "/posts" + "/delete";
    public static final String URL_UPDATE_POST = DOMAIN + "/posts" + "/update";

}
