package mainPackage.loginService;

import mainPackage.dbmsPackage.ConnectToDB;
import mainPackage.usersPackage.GeneralUser;
import mainPackage.usersPackage.GeneralUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        GeneralUser user = generalUserRepository.findGeneralUserByEmailAndPassword(generalUser.getUserName(), generalUser.getPassword());
        if(user == null){
            return "user does not exist OR password incorrect password";
        }
        return "welcome back! " + user.getUserName() ;
    }

    /*REMOVE METHODS BELOW AFTER DEMO2*/

    //list/read
    @GetMapping("/login/getUsers/{mode}")
    public ArrayList<GeneralUser> getUsers(@PathVariable(name="mode") int mode){
        if(mode > 3 || mode < 0)return null;
        if(mode == 0){
            return generalUserRepository.findGeneralUsersByUserType(0);
        }
        if(mode == 1){
            return generalUserRepository.findGeneralUsersByUserType(1);
        }
        if(mode == 2){
            return generalUserRepository.findGeneralUsersByUserType(2);
        }
       return generalUserRepository.findAll();
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
    public Object updateUser(@RequestBody ArrayList<GeneralUser> old2new){
        if(old2new.size() != 2)return null;

        GeneralUser old = old2new.get(0);
        GeneralUser newUser = old2new.get(1);

        GeneralUser db = generalUserRepository.findGeneralUserByEmailAndPassword(old.getEmail(),old.getPassword());

        if(db == null){
            return "user does not exist";
        }
        if(generalUserRepository.findGeneralUserByEmail(newUser.getEmail()) != null && !(db.getEmail().equals(newUser.getEmail())) ){
            return "email already exists";
        }

        updateUser(db,newUser);
        return db;

    }

    //delete
    @PostMapping("/login/deleteUser")
    public Object deleteUser(@RequestBody GeneralUser delUser){
        GeneralUser db = generalUserRepository.findGeneralUserByEmailAndPassword(delUser.getEmail(),delUser.getPassword());
        if(db == null)return "user cannot be delete because it does not exist";
        String msg = db.getUserName() + " was successfully deleted";
        generalUserRepository.delete(db);
        return msg;
    }














}
