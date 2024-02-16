package com.cs309.ta45.backend.mainPackage.signupService;

import com.cs309.ta45.backend.mainPackage.dbmsPackage.Connect2DBHibernate;

import com.cs309.ta45.backend.mainPackage.usersPackage.GeneralUser;
import com.cs309.ta45.backend.mainPackage.usersPackage.GeneralUserRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



/**
 * @author Junhyung Shim
 * This controller will handle the get/post data for signup
 * for post data it will create a user to db
 * */
@RestController
public class SignUpController {

    @Autowired
    private GeneralUserRepository generalUserRepository;
    private void createUserInDB(GeneralUser generalUser){
        try{
            Connect2DBHibernate sess = new Connect2DBHibernate();
            Session currSess = sess.getSession();
            currSess.beginTransaction();
            currSess.persist(generalUser);
            currSess.getTransaction().commit();
            currSess.close();
        } catch (Exception e) {
            System.out.println("Error at SignUpController.createUserInDB()");
            e.printStackTrace();
        }

    }

    @PostMapping("/signup")
    public String createUser(@RequestBody GeneralUser generalUser){
        generalUserRepository.save(generalUser);
        return "Welcome! " + generalUser.getFirstName();
    }






}
