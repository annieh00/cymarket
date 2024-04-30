package mainPackage.userRatingsService;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import mainPackage.usersPackage.GeneralUser;

@TableGenerator(
        name = "RatingGenerator",
        allocationSize = 1,
        initialValue = 1)
@Entity
@Table(name="Rating")
public class Rating {
    @Expose
    @GeneratedValue(
            strategy=GenerationType.TABLE,
            generator="RatingGenerator")
    @Id
    @Column(name = "rid")
    private int rid;

    @Expose
    @Column(name = "author_username")
    private String authorUsername;


    @Expose
    @Column(name = "description")
    private String description;


    @Expose
    @Column(name = "stars")
    private int stars;

    public String getRevieweeUserName() {
        return revieweeUserName;
    }

    public void setRevieweeUserName(String revieweeUserName) {
        this.revieweeUserName = revieweeUserName;
    }

    @Column(name = "revieweeUserName")
    private String revieweeUserName;





    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String autorUsername) {
        this.authorUsername = autorUsername;
    }



}
