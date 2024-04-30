package mainPackage.usersPackage;

import jakarta.persistence.*;
import mainPackage.usersPackage.Posting;

@Entity
@TableGenerator(
        name = "viewedPostGenerator",
        allocationSize = 1,
        initialValue = 1)
@Table(name = "viewed_post_history")
public class ViewedPostHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "viewedPostGenerator")
    @Column(name = "viewId")
    private int id;

    @ManyToOne
    @JoinColumn(name = "uid")
    private GeneralUser user;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Posting post;

    public int getId() {
        return id;
    }

    public GeneralUser getUser() {
        return user;
    }

    public void setUser(GeneralUser user) {
        this.user = user;
    }

    public Posting getPost() {
        return post;
    }

    public void setPost(Posting post) {
        this.post = post;
    }
}
