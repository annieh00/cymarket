package mainPackage.usersPackage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

/**
 * @author Junhyung Shim
 * */
public interface GeneralUserRepository extends JpaRepository<GeneralUser,Long> {
    public GeneralUser findGeneralUserByUserName(String uid);
    public ArrayList<GeneralUser> findGeneralUsersByUserType(int userType);
    public GeneralUser findGeneralUserByEmailAndPassword(String email, String password);

    public GeneralUser findById(int id);
    public GeneralUser findGeneralUserByEmail(String email);


    public ArrayList<GeneralUser> findAll();

}
