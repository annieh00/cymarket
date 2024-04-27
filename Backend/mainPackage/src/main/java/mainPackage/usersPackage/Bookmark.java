package mainPackage.usersPackage;

import jakarta.persistence.*;

@Entity
@TableGenerator(
        name = "bookmarkGenerator",
        allocationSize = 1,
        initialValue = 1)
@Table(name = "bookmarks")
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "bookmarkGenerator")
    @Column(name = "bookmarkId")
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
