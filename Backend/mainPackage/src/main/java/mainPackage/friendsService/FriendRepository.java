package mainPackage.friendsService;

import mainPackage.usersPackage.GeneralUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    Friend findFriendBySenderAndReceiver(GeneralUser sender, GeneralUser receiver);

    // Retrieve friend requests received by a specific user, excluding those with status 'ACCEPTED' or 'DECLINED'
    @Query("SELECT f FROM Friend f " +
            "WHERE f.receiver = :user " +
            "AND f.status <> 'ACCEPTED' " +
            "AND f.status <> 'DECLINED'")
    List<Friend> findFriendRequestsByReceiver(GeneralUser user);
}
