package com.cs309.ta45.backend.mainPackage.usersPackage;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

/**
 * @author Junhyung Shim
 * */
public interface GeneralUserRepository extends CrudRepository<GeneralUser,Long> {
    public GeneralUser findGeneralUserByUserName(String uid);
    public ArrayList<GeneralUser> findGeneralUsersByUserType(int userType);
    public GeneralUser findGeneralUserByEmailAndPassword(String email, String password);

    public GeneralUser findGeneralUserByEmail(String email);


    public ArrayList<GeneralUser> findAll();

}
