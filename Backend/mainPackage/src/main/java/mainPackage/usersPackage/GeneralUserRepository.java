package mainPackage.usersPackage;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Junhyung Shim
 * */
@Repository
public interface GeneralUserRepository extends JpaRepository<GeneralUser,Long> {
    public GeneralUser findGeneralUserByUserName(String userName);
    public ArrayList<GeneralUser> findGeneralUsersByUserType(int userType);
    public GeneralUser findGeneralUserByEmailAndPassword(String email, String password);
    public GeneralUser findById(int id);
    public GeneralUser findGeneralUserByEmail(String email);
    public ArrayList<GeneralUser> findAll();
    public GeneralUser findGeneralUserById(int id);
    @Query("SELECT u FROM GeneralUser u " +
            "WHERE u.id <> :id " + // Exclude the user themselves
            "AND u.userType = 2 " + // Include only users with userType 2 (normal users)
            "AND u NOT IN (SELECT f.sender FROM Friend f WHERE f.receiver.id = :id AND f.status = 'PENDING')" + // Exclude users who sent pending friend requests to the user
            "AND u NOT IN (SELECT f.receiver FROM Friend f WHERE f.sender.id = :id AND f.status = 'PENDING')" + // Exclude users who received pending friend requests from the user
            "AND u NOT IN (SELECT f.sender FROM Friend f WHERE f.receiver.id = :id AND f.status = 'ACCEPTED')" + // Exclude users who are already friends with the user
            "AND u NOT IN (SELECT f.receiver FROM Friend f WHERE f.sender.id = :id AND f.status = 'ACCEPTED')" // Exclude users who are already friends with the user
    )
    List<GeneralUser> findPotentialFriends(@Param("id") int id);
}
