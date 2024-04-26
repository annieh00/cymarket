package mainPackage.usersPackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Junhyung Shim
 * */
@Repository
public interface PostingRepository extends JpaRepository<Posting,Long> {
    ArrayList<Posting> findPostingByUserName(String author);
    Posting findPostingById(int id);

    Posting findPostingByTitle(String title);

    ArrayList<Posting> findPostingsByTitle(String title);

    List<Posting> findByUserNameContainingIgnoreCase(String query);

    List<Posting> findByTitleContainingIgnoreCase(String title);

    List<Posting> findByDescriptionContainingIgnoreCase(String description);

    @Query("SELECT p FROM Posting p WHERE :category MEMBER OF p.categories")
    List<Posting> findByCategory(String category);


}
