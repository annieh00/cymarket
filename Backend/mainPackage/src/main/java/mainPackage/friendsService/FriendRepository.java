package mainPackage.friendsService;

import mainPackage.usersPackage.GeneralUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {


    // Used in accept/reject. Person who is accepting/rejecting is the receiver. Have to switch parameters.
    @Query("SELECT f FROM Friend f " +
            "WHERE (f.sender = :sender AND f.receiver = :receiver) OR " +
            "(f.sender = :receiver AND f.receiver = :sender)")
    Friend findFriendBySenderAndReceiver(GeneralUser sender, GeneralUser receiver);

    // Retrieve friend requests received by a specific user, including those with PENDING status'
    @Query("SELECT f FROM Friend f " +
            "WHERE f.receiver = :user " +
            "AND f.status = 'PENDING'")
    List<Friend> findFriendRequestsByReceiver(GeneralUser user);

    @Query("SELECT f FROM Friend f WHERE (f.sender.id = :senderId AND f.receiver.id = :receiverId) " +
            "OR (f.sender.id = :receiverId AND f.receiver.id = :senderId) AND f.status = 'PENDING'")
    Friend findPendingRequest(int senderId, int receiverId);

    @Query("SELECT f.sender FROM Friend f WHERE f.receiver = :u AND f.status = 'ACCEPTED'")
    List<GeneralUser> findFriendsByReceiver(@Param("u") GeneralUser u);

    @Query("SELECT f.receiver FROM Friend f WHERE f.sender = :u AND f.status = 'ACCEPTED'")
    List<GeneralUser> findFriendsBySender(@Param("u") GeneralUser u);
}
