package mainPackage.usersPackage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Junhyung Shim
 * */

@TableGenerator(
        name = "postsGenerator",
        allocationSize = 1,
        initialValue = 1)
@Entity
@Table(name = "posts")
public class Posting {

    @Expose
    //@OneToOne(cascade = CascadeType.ALL)
    @Column(name = "userName")
    @JoinColumn(name="email")
    private String userName;

    @Expose
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;

    @Expose
    @ElementCollection
    @CollectionTable(
            name = "post_categories",
            joinColumns = @JoinColumn(name = "postId")
    )
    @Column(name = "categories")
    private List<String> categories;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "viewed_post_history",
            joinColumns = @JoinColumn(name = "postId"),
            inverseJoinColumns = @JoinColumn(name = "uid")
    )
    @JsonIgnore
    private List<GeneralUser> viewedUsers = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_post_bookmarks",
            joinColumns = @JoinColumn(name = "postId"),
            inverseJoinColumns = @JoinColumn(name = "uid")
    )
    @JsonIgnore
    private List<GeneralUser> usersBookmarked = new ArrayList<>();

    @Expose
    @Column(name = "isAuction")
    private boolean isAuction = false;

    @Expose
    @Column(name = "isDonation")
    private boolean isDonation = false;

    @Expose
    @Column(name = "isClosed")
    private boolean isClosed = false;
    @Expose
    @Column(name = "picture1")
    private String picture1;
    @Expose
    @Column(name = "picture2")
    private String picture2;

    @Expose
    @Column(name = "picture3")
    private String picture3;

    @Expose
    @Column(name = "picture4")
    private String picture4;

    @Expose
    @Column(name = "picture5")
    private String picture5;

    @Expose
    @Column(name = "picture6")
    private String picture6;

    @Expose
    @Column(name = "price")
    private int price;

    @Expose
    @Column(name = "PublishedDate")
    String date;

    @Id
    @GeneratedValue(
            strategy=GenerationType.TABLE,
            generator="usersGenerator")
    @Column(name = "postId")
    private int id;

    public String getUserName() {
        return userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(boolean closed) {
        isClosed = closed;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    public String getPicture1() {
        return picture1;
    }

    public void setPicture1(String picture1) {
        this.picture1 = picture1;
    }

    public String getPicture2() {
        return picture2;
    }

    public void setPicture2(String picture2) {
        this.picture2 = picture2;
    }

    public String getPicture3() {
        return picture3;
    }

    public void setPicture3(String picture3) {
        this.picture3 = picture3;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getIsAuction() {
        return isAuction;
    }
    public void setIsAuction(boolean auction) {
        isAuction = auction;
    }

/*
    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }


    public int getTimeAliveInMinutes() {
        return timeAliveInMinutes;
    }

    public void setTimeAliveInMinutes(int timeAliveInMinutes) {
        this.timeAliveInMinutes = timeAliveInMinutes;
    }
*/

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPicture4() {
        return picture4;
    }

    public void setPicture4(String picture4) {
        this.picture4 = picture4;
    }

    public String getPicture5() {
        return picture5;
    }

    public void setPicture5(String picture5) {
        this.picture5 = picture5;
    }

    public String getPicture6() {
        return picture6;
    }

    public void setPicture6(String picture6) {
        this.picture6 = picture6;
    }

    public boolean getIsDonation() {
        return isDonation;
    }

    public void setIsDonation(boolean donation) {
        isDonation = donation;
    }

    public void setCategories(List<String> categories) { this.categories = categories; }
    public List<String> getCategories() { return this.categories; }

    public List<GeneralUser> getViewedUsers() {
        return viewedUsers;
    }

    public List<GeneralUser> getUsersBookmarked() {
        return usersBookmarked;
    }

    public void setUsersBookmarked(List<GeneralUser> usersBookmarked) {
        this.usersBookmarked = usersBookmarked;
    }
}
