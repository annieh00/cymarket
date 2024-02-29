package mainPackage.usersPackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * @author Junhyung Shim
 * */
@Repository
public interface GeneralUserRepository extends JpaRepository<GeneralUser,Long> {
    public GeneralUser findGeneralUserByUserName(String uid);
    public ArrayList<GeneralUser> findGeneralUsersByUserType(int userType);
    //@Query(value = "SELECT * FROM USERS WHERE email = ?0 AND password = ?1",nativeQuery = true)
    public GeneralUser findGeneralUserByEmailAndPassword(String email, String password);


    public GeneralUser findById(int id);
    public GeneralUser findGeneralUserByEmail(String email);


    public ArrayList<GeneralUser> findAll();

}
