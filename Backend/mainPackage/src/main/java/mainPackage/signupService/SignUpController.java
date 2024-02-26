package mainPackage.signupService;

import mainPackage.usersPackage.GeneralUser;
import mainPackage.usersPackage.GeneralUserRepository;
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

    //create
    @PostMapping("/signup")
    public String createUser(@RequestBody GeneralUser generalUser){
        generalUserRepository.save(generalUser);
        return "Welcome! " + generalUser.getFirstName();
    }







}
