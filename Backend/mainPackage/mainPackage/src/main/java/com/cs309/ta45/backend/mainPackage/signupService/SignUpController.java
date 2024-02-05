package com.cs309.ta45.backend.mainPackage.signupService;

import com.cs309.ta45.backend.mainPackage.dbmsPackage.ConnectToDB;
import com.cs309.ta45.backend.mainPackage.usersPackage.GeneralUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Junhyung Shim
 * This controller will handle the get/post data for signup
 * for post data it will create a user to db
 * */
@RestController
public class SignUpController {

    private void createUserInDB(Connection con, GeneralUser generalUser){
        try{
            Statement st = con.createStatement();
            //firstName, lastName, email, password, isNormal(int), isOrg(int), isManaager(int)
            int isNormal = 0;
            int isOrg = 0;
            int isManager = 0;

            if(generalUser.getUserType() == 0){
                isNormal = 1;
            }else if(generalUser.getUserType() == 1){
                isOrg = 1;
            }else if(generalUser.getUserType() == 2){
                isManager = 1;
            }else{//input error
                System.out.println("NOT A USER TYPE");
                isNormal = 1;
            }

            String insertSql =
                    "INSERT INTO USERS(firstName, lastName, email, password,isNormal,isOrg,isManager) VALUES("
                            +"\""+generalUser.getFirstName()+"\""+","
                            +"\""+generalUser.getLastName() +"\"" + ","
                            +"\""+generalUser.getEmail() +"\"" + ","
                            +"\""+generalUser.getPassword() +"\"" + ","
                            +isNormal + ","
                            +isOrg + ","
                            +isManager + ")";
            System.out.println(insertSql);
            st.executeUpdate(insertSql);

        } catch (SQLException e) {
            System.out.println("Error at SignUpController.createUserInDB()");
            e.printStackTrace();
        }

    }

    @PostMapping("/signup")
    public String createUser(@RequestBody GeneralUser generalUser){
        ConnectToDB db = new ConnectToDB(ConnectToDB.getOneTimeConnection());
        Connection con = db.getCurrentConnection();
        createUserInDB(con,generalUser);
        return "Welcome! " + generalUser.getFirstName();
    }






}
