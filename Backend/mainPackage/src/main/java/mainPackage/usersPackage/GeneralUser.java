package mainPackage.usersPackage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import mainPackage.announcementPackage.Announcement;
import mainPackage.friendsService.Friend;
import mainPackage.userRatingsService.Rating;
import mainPackage.websocket.AuctionTable;
import mainPackage.websocket.Message;

import javax.swing.text.View;
import java.awt.print.Book;
import java.util.*;

/**
 * @author Junhyung Shim
 * implementation of normal user
 * Watch out for getter and setter names, it might cause errors
 * */
@TableGenerator(
        name = "usersGenerator",
        allocationSize = 1,
        initialValue = 1)
@Entity
@Table(name="users")
public class GeneralUser {


    @Expose
    @Column(name = "firstName")
    private String firstName;

    @Expose
    @Column(name = "lastName")
    private String lastName;

    @Expose
    @Column(name = "email",unique = true)
    private String email;

    @Expose
    @GeneratedValue(
            strategy=GenerationType.TABLE,
            generator="usersGenerator")
    @Id
    @Column(name = "uid")
    private int id;

    @Expose
    @Column(name = "password")
    private String password;

    @Expose
    @Column(name="userType")
    private int userType;

    @Expose
    @Column(name = "userName", unique = true)
    private String userName;

    @Expose
    @Column(name = "searchHistory")
    @ElementCollection
    @CollectionTable(
            name = "searchHistory_user",
            joinColumns = @JoinColumn(name = "uid")
    )
    private List<String> searchHistory;

    @OneToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private List<ViewedPostHistory> viewedPostHistory;

    @OneToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Bookmark> postBookmarks;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<AuctionTable> connectedSessions = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Rating> myRatings;

    @Expose
    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Posting> publishedPosts = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Announcement> announcements = new ArrayList<>() { };

    @OneToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Message> userSent;

    @OneToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Message> userReceived;

    @OneToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Friend> friendships;


    public Set<AuctionTable> getConnectedSessions() {
        return connectedSessions;
    }

    public void setConnectedSessions(Set<AuctionTable> connectedSessions) {
        this.connectedSessions = connectedSessions;
    }

    public void addConnectedSessions(AuctionTable a){
        this.connectedSessions.add(a);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public Set<Posting> getPublishedPosts() {
        return publishedPosts;
    }

    public void setPublishedPosts(Set<Posting> publishedPosts) {
        this.publishedPosts = publishedPosts;
    }

    public List<Announcement> getAnnouncements() { return this.announcements; }

    public List<Rating> getMyRatings() {
        return myRatings;
    }

    public void setMyRatings(List<Rating> myRatings) {
        this.myRatings = myRatings;
    }

    public List<String> getSearchHistory() {
        return searchHistory;
    }

    public void setSearchHistory(List<String> searchHistory) {
        this.searchHistory = searchHistory;
    }

    public List<ViewedPostHistory> getViewedPostHistory() {
        return viewedPostHistory;
    }

    public List<Bookmark> getPostBookmarks() {
        return postBookmarks;
    }

    public void setPostBookmarks(List<Bookmark> postBookmarks) {
        this.postBookmarks = postBookmarks;
    }
}
