package mainPackage.userRatingsService;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import mainPackage.usersPackage.GeneralUser;

@TableGenerator(
        name = "RatingGenerator",
        allocationSize = 1,
        initialValue = 1)
@Entity
@Table(name="Rating")
public class Rating {

    @GeneratedValue(
            strategy=GenerationType.TABLE,
            generator="RatingGenerator")
    @Id
    @Column(name = "rid")
    private int rid;


    @Column(name = "author_username")
    private String authorUsername;



    @Column(name = "description")
    private String description;



    @Column(name = "stars")
    private int stars;





    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "userName")
    private GeneralUser reviewee;

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public GeneralUser getReviewee() {
        return reviewee;
    }

    public void setReviewee(GeneralUser reviewee) {
        this.reviewee = reviewee;
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
