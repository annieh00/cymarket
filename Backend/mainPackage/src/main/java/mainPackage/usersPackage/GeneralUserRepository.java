package mainPackage.usersPackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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

    public List<GeneralUser> findAllFriends();

    void deleteFriend(GeneralUser u);
}
