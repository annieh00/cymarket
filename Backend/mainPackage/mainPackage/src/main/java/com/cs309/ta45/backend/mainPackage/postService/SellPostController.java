package com.cs309.ta45.backend.mainPackage.postService;

import com.cs309.ta45.backend.mainPackage.dbmsPackage.ConnectToDB;
import com.cs309.ta45.backend.mainPackage.usersPackage.GeneralUser;
import com.cs309.ta45.backend.mainPackage.usersPackage.Posting;
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
public class SellPostController {
    private void createPost(Connection con, Posting p){
        try{
            Statement st = con.createStatement();
            //firstName, lastName, email, password, isNormal(int), isOrg(int), isManaager(int)


            String insertSql =
                    "INSERT INTO posts(author, description, picture1, picture2,picture3,picture4,picture5,picture6,suspicious) VALUES("
                            +"\""+p.getAuthor()+"\""+","
                            +"\""+p.getDescription() +"\"" + ","
                            +"\""+p.getPicture1() +"\"" + ","
                            +"\""+p.getPicture2() +"\"" + ","
                            +"\""+p.getPicture3() +"\"" + ","
                            +"\""+p.getPicture4() +"\"" + ","
                            +"\""+p.getPicture5() +"\"" + ","
                            +"\""+p.getPicture6() +"\"" + ","
                            +"0" + ")";
            System.out.println(insertSql);
            st.executeUpdate(insertSql);

        } catch (SQLException e) {
            //System.out.println("Error at SignUpController.createUserInDB()");
            e.printStackTrace();
        }

    }



    @PostMapping("/postSell")
    public Posting createUser(@RequestBody Posting p){
        ConnectToDB db = new ConnectToDB(ConnectToDB.getOneTimeConnection());
        Connection con = db.getCurrentConnection();
        createPost(con,p);
        db.closeConnection();
        return p;
    }






}
