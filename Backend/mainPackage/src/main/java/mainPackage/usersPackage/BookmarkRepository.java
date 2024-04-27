package mainPackage.usersPackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Bookmark findBookmarkById(int bid);

    Bookmark findByUserAndPost(GeneralUser user, Posting post);

    List<Bookmark> findAllByUser(GeneralUser user);
}
