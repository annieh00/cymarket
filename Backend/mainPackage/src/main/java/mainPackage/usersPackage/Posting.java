package mainPackage.usersPackage;

import jakarta.persistence.*;
import lombok.Getter;

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

    //@OneToOne(cascade = CascadeType.ALL)
    @Column(name = "userName")
    //@JoinColumn(name="email")
    private String userName;

    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;



    @Column(name = "isAuction")
    private boolean isAuction;

    @Column(name = "picture1")
    private String picture1;

    @Column(name = "picture2")
    private String picture2;

    @Column(name = "picture3")
    private String picture3;
//    @Column(name = "picture4")
//    private String picture4;
//
//    @Column(name = "picture5")
//    private String picture5;
//
//    @Column(name = "picture6")
//    private String picture6;

    @Column(name = "timeAliveInMinutes")
    private int timeAliveInMinutes;

    @Id
    @GeneratedValue(
            strategy=GenerationType.TABLE,
            generator="usersGenerator")
    @Column(name = "postId")
    private int id;
    public String getUserName() {
        return userName;
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

    public boolean getIsAuction() {
        return isAuction;
    }
    public void setIsAuction(boolean auction) {
        isAuction = auction;
    }



    public int getTimeAliveInMinutes() {
        return timeAliveInMinutes;
    }

    public void setTimeAliveInMinutes(int timeAliveInMinutes) {
        this.timeAliveInMinutes = timeAliveInMinutes;
    }

//    public String getPicture4() {
//        return picture4;
//    }
//
//    public void setPicture4(String picture4) {
//        this.picture4 = picture4;
//    }
//
//    public String getPicture5() {
//        return picture5;
//    }
//
//    public void setPicture5(String picture5) {
//        this.picture5 = picture5;
//    }
//
//    public String getPicture6() {
//        return picture6;
//    }
//
//    public void setPicture6(String picture6) {
//        this.picture6 = picture6;
//    }
}
