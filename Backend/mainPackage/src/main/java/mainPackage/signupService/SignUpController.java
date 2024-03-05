package mainPackage.signupService;

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

    //create
    @PostMapping("/signup")
    public String createUser(@RequestBody GeneralUser generalUser){
        try{
            if(generalUserRepository.findGeneralUserByEmail(generalUser.getEmail()) != null){
                return "{\"fromServer\" : false}";
            }

            generalUserRepository.save(generalUser);
        }catch (Exception e){
            e.printStackTrace();
            return "{\"fromServer\" : false}";
        }

        return "{\"fromServer\" : true}";
    }







}
