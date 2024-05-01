package mainPackage.loginService;

import com.google.gson.GsonBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.websocket.server.PathParam;
import mainPackage.dbmsPackage.ConnectToDB;
import mainPackage.userRatingsService.Rating;
import mainPackage.userRatingsService.RatingRepository;
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
import java.util.HashMap;
import java.util.List;

/**
 * @author Junhyung Shim
 * This controller will handle the post data for login
 * for post data it will create a user to db
 * */
@RestController
public class LogInController {


    @Autowired
    private GeneralUserRepository generalUserRepository;

    @Autowired
    private RatingRepository ratingRepository;


    @Operation(summary = "Check for login", description = "Checks whether given user data is in the DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "There is a user in DB where it matches the username and password; returns true", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "user does not exist in DB")
    })
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
        // 0 admin
        // 1 organization
        // 2 normal user
        return "{\"fromServer\" : true, \"permission\" : " + user.getUserType() + ",\"username\" : " + user.getUserName() + ",\"id\" : " + user.getId() + "}";
    }

    /*REMOVE METHODS BELOW AFTER DEMO2*/

    //list/read
    @Operation(summary = "lists all users in the DB", description = "lists all users in the DB, used for admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "returns JSON array of users", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "401", description = "Unathorized access: not admin")
    })
    @GetMapping("/login/getAllUsers")
    public String getUsers(){
        ArrayList<GeneralUser> mylist = generalUserRepository.findAll();
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(mylist);
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
    @Operation(summary = "updates specific user in DB", description = "updates specific user in DB, password is required")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "user updated successfully", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "No match for given user data found"),
            @ApiResponse(responseCode = "401", description = "Unathorized access, you are not the user")

    })
    @PutMapping("/login/editUser")
    public String updateUser(@RequestBody HashMap<String,ArrayList<GeneralUser>> editList){

        ArrayList<GeneralUser> arr = editList.get("array");
        GeneralUser before = arr.get(0);
        GeneralUser after = arr.get(1);

        System.out.println(before.getEmail());
        System.out.println(before.getPassword());

        GeneralUser db = generalUserRepository.findGeneralUserByEmailAndPassword(before.getEmail(), before.getPassword());
        if(db == null){
            return "{\"response\" : " + "\"user does not exist\"" +"}";
        }
        String oldP = before.getPassword();
        String newP = after.getPassword();
        db.setPassword(after.getPassword());
        generalUserRepository.save(db);




        //updateUser(db,newUser);
        return "{\"response\" : " + "\"" +"pw changed from "+ oldP + " to " + newP +"\"" +"}";

    }

    //delete
    @GetMapping("/login/deleteUser/{userName}")
    public String deleteUser(@PathVariable(name = "userName") String userName){
        GeneralUser db = generalUserRepository.findGeneralUserByUserName(userName);
        if(db == null){
            return "{\"deleteUser\" : false}";
        }

        //String msg = db.getUserName() + " was successfully deleted";
        List<Rating> list = db.getMyRatings();
        if(list != null && !list.isEmpty()){
            for(Rating r : list){
                ratingRepository.deleteById((long) r.getRid());
            }
        }
        generalUserRepository.delete(db);
        return "{\"deleteUser\" : true}";
    }

    @Operation(summary = "deletes specific user in DB", description = "deletes specific user in DB, password is required")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "user deleted successfully", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "No match for given user data found"),
            @ApiResponse(responseCode = "401", description = "Unathorized deletion, you are not the user")

    })
    @PostMapping("/deleteUser")
    public String deleteUser(@RequestBody GeneralUser userToEdit){
        System.out.println(userToEdit.getEmail());
        System.out.println(userToEdit.getPassword());
        GeneralUser db = generalUserRepository.findGeneralUserByUserName(userToEdit.getUserName());
        if(db == null){
            return "{\"deleteUser\" : false}";
        }
        //String msg = db.getUserName() + " was successfully deleted";
        //System.out.println(db.getUserName());
        generalUserRepository.delete(db);

        return "{\"deleteUser\" : true}";
    }

    @GetMapping("/getUserID/{userName}")
    public String getUserID(@PathVariable(name = "userName") String userName){
        GeneralUser u = generalUserRepository.findGeneralUserByUserName(userName);
        if(u == null){
            return "{\"id\" : -1}"; //sentinel to denote user does not exist
        }else{
            return "{\"id\" : " + u.getId()+ "}";
        }
    }














}
