package mainPackage.userRatingsService;

import mainPackage.usersPackage.GeneralUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Long> {
    Rating findRatingByAuthorUsername(String authorUserName);
    List<Rating> findRatingsByReviewee(GeneralUser reviewee);

}
