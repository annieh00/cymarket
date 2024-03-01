package mainPackage.usersPackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
/**
 * @author Junhyung Shim
 * */
@Repository
public interface PostingRepository extends JpaRepository<Posting,Long> {
    ArrayList<Posting> findPostingByUserName(String author);
    Posting findPostingById(int id);

    public ArrayList<Posting> findAll();
}
