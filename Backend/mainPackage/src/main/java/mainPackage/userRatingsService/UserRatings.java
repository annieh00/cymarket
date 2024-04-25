package mainPackage.userRatingsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import mainPackage.usersPackage.GeneralUser;
import mainPackage.usersPackage.GeneralUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
public class UserRatings {

    @Autowired
    private static GeneralUserRepository generalUserRepository;

    @Autowired
    private static RatingRepository ratingRepository;
    @Operation(summary = "updates the user's rating", description = "user rating is between 0 to 5")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "user rating was set successfully", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "invalid input")
    })
    //create
    @PostMapping("/setUserRating/{userName}")
    public String setUserRating(@PathVariable("userName") String userName, @RequestBody Rating r){

        GeneralUser u = generalUserRepository.findGeneralUserByUserName(userName);
        if(u == null){
            return "{\"serverResponse\":false}";
        }
        if(r.getDescription() != null && !r.getDescription().isEmpty()){
            r.setReviewee(u);
            ratingRepository.save(r);
            Set<Rating> setOfRatings = u.getMyRatings();
            setOfRatings.add(r);
            generalUserRepository.save(u);
            return "{\"serverResponse\":true}";
        }

        return "{\"serverResponse\":false}";
    }




}
