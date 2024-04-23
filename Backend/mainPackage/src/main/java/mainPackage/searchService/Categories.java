package mainPackage.searchService;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import mainPackage.usersPackage.Posting;

import java.util.HashSet;
import java.util.Set;

@TableGenerator(
        name = "categoryGenerator",
        allocationSize = 1,
        initialValue = 1)
@Entity
@Table(name = "categories")
public class Categories {

    @Id
    @GeneratedValue(
            strategy=GenerationType.TABLE,
            generator="categoryGenerator")
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Posting> postings = new HashSet<>();

    public Categories(String name) {
        this.name = name;
    }

    public Categories() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Posting> getPostings() {
        return postings;
    }

    public void setPostings(Set<Posting> postings) {
        this.postings = postings;
    }

    // Add convenience methods for adding/removing Postings
    public void addPosting(Posting posting) {
        this.postings.add(posting);
        posting.getCategories().add(this);
    }

    public void removePosting(Posting posting) {
        this.postings.remove(posting);
        posting.getCategories().remove(this);
    }
}
