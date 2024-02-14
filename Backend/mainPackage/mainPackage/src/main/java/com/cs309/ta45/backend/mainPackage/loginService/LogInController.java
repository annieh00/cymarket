package com.cs309.ta45.backend.mainPackage.loginService;

import com.cs309.ta45.backend.mainPackage.dbmsPackage.ConnectToDB;
import com.cs309.ta45.backend.mainPackage.usersPackage.GeneralUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Junhyung Shim
 * This controller will handle the post data for login
 * for post data it will create a user to db
 * */
@RestController
public class LogInController {

    private String checkIfUserInDB(Connection con, GeneralUser generalUser){
        try{
            Statement st = con.createStatement();
            //firstName, lastName, email, password, isNormal(int), isOrg(int), isManaager(int)

            String find = "select * from users where"+
                     " userName =" + "\"" +generalUser.getUserName() +"\"";

            ResultSet rs = st.executeQuery(find);
            if(!rs.isBeforeFirst()){//checks if the cursor is before the table
                return "user does not exist";
            }else{
                rs.next();
                String password = rs.getString("password");
                System.out.println(password);
                if(password.equals(generalUser.getPassword())){
                    return "welcome back " + rs.getString("firstName");
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return "something went wrong";

    }

    @PostMapping("/login")
    public String createUser(@RequestBody GeneralUser generalUser){
        ConnectToDB db = new ConnectToDB(ConnectToDB.getOneTimeConnection());
        Connection con = db.getCurrentConnection();
        String ret = checkIfUserInDB(con,generalUser);
        db.closeConnection();
        return ret;
    }






}
