package mainPackage.loginService;

import mainPackage.dbmsPackage.ConnectToDB;
import mainPackage.usersPackage.GeneralUser;
import mainPackage.usersPackage.GeneralUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author Junhyung Shim
 * This controller will handle the post data for login
 * for post data it will create a user to db
 * */
@RestController
public class LogInController {


    @Autowired
    private GeneralUserRepository generalUserRepository;

    //read
    @PostMapping("/login")
    public String checkUser(@RequestBody GeneralUser generalUser){

        GeneralUser user = generalUserRepository.findGeneralUserByEmail(generalUser.getEmail());
        System.out.println(generalUser.getEmail());
        System.out.println(generalUser.getPassword());
        System.out.println(user);

        if(user == null || !(user.getPassword().equals(generalUser.getPassword()))){
            return "{\"fromServer\" : false}";
        }
        //return "welcome back! " + user.getUserName() ;
        System.out.println("returned true!");
        return "{\"fromServer\" : true}";
    }

    /*REMOVE METHODS BELOW AFTER DEMO2*/

    //list/read
    @GetMapping("/login/getAllUsers")
    public String getUsers(){
        ArrayList<GeneralUser> mylist = generalUserRepository.findAll();
        String json = new Gson().toJson(mylist);

        return "{ \"users\" :" +json + "}";
    }

    private void updateUser(GeneralUser db, GeneralUser req){
        if(req.getUserName() != null){
            db.setUserName(req.getUserName());
        }
        if(req.getPassword() != null){
            db.setPassword(req.getUserName());
        }

        if(req.getFirstName() != null){
            db.setFirstName(req.getFirstName());
        }
        if(req.getLastName() != null){
            db.setLastName(req.getLastName());
        }

        generalUserRepository.save(db);

    }

    //update
    @PostMapping("/login/editUser")
    public Object updateUser(@RequestBody GeneralUser userToEdit){

        GeneralUser editedUser;

        GeneralUser db = generalUserRepository.findGeneralUserByEmailAndPassword(userToEdit.getEmail(),userToEdit.getPassword());
        db.setUserName(userToEdit.getUserName());
        generalUserRepository.save(db);
        if(db == null){
            return "{\"response\" : " + db.getUserName() +"}";
        }


        //updateUser(db,newUser);
        return db;

    }

    //delete
    @DeleteMapping("/login/deleteUser")
    public String deleteUser(@RequestBody GeneralUser delUser){
        GeneralUser db = generalUserRepository.findGeneralUserByEmail(delUser.getEmail());
        if(db == null){
            return "{\"deleteUser\" : false}";
        }

        //String msg = db.getUserName() + " was successfully deleted";
        generalUserRepository.delete(db);
        return "{\"deleteUser\" : true}";
    }














}
