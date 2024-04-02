package mainPackage.signupService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import mainPackage.usersPackage.GeneralUser;
import mainPackage.usersPackage.GeneralUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


/**
 * @author Junhyung Shim
 * This controller will handle the get/post data for signup
 * for post data it will create a user to db
 * */
@RestController
public class SignUpController {

    @Autowired
    private GeneralUserRepository generalUserRepository;
    @Operation(summary = "create account for a user", description = "creates a user in DB, type specified by an int (0,1) corresponding to (normal, organization), admin cannot sign up")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "user created successfully", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "user already signed up with the email or illegal fields were given")
    })
    //create
    @PostMapping("/signup")
    public String createUser(@RequestBody GeneralUser generalUser){
        try{
            if(generalUserRepository.findGeneralUserByEmail(generalUser.getEmail()) != null){
                return "{\"fromServer\" : false}";
            }
            generalUser.setUserType(2);
            generalUserRepository.save(generalUser);
        }catch (Exception e){
            e.printStackTrace();
            return "{\"fromServer\" : false}";
        }

        return "{\"fromServer\" : true}";
    }







}
