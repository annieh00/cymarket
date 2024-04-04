package com.example.androidexample;

//import CurrentUser.usersPackage.GeneralUser;


//CLASS TO REPRESENT THE CURRENT USER THAT IS LOGGED IN

public class CurrentUser {

    private static String currentUser;

    // Method to set the current logged-in user
    public static void setCurrentUser(String user) {
        currentUser = user;
    }

    // Method to get the current logged-in user
    public static String getCurrentUser() {
        return currentUser;
    }

    // Method to check if a user is currently logged in
    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    // Method to log out the current user
    public static void logout() {
        currentUser = null;
    }
}