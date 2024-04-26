package mainPackage.userRatingsService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import mainPackage.usersPackage.GeneralUser;
import mainPackage.usersPackage.GeneralUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
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

    @GetMapping("/getAllRatings")
    public String getAllRatings(){
        List<Rating> lr = ratingRepository.findAll();

        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.setPrettyPrinting().create();
        String json = gson.toJson(lr);
        return "{\"ratings\":" + json + "}";
    }

    @GetMapping("/getRatingsOf/{userName}")
    public String getRatings(@PathVariable(name = "userName") String userName){
        GeneralUser u = generalUserRepository.findGeneralUserByUserName(userName);
        if(u != null){
            List<Rating> lr =  ratingRepository.findRatingsByReviewee(u);
            GsonBuilder builder = new GsonBuilder();
            builder.serializeNulls();
            Gson gson = builder.setPrettyPrinting().create();
            String json = gson.toJson(lr);
            return "{\"ratings\" : " + json + "}";
        }
        return "{\"ratings\" : null }";
    }

    @PostMapping("/deleteRating/{author}/{userName}")
    public String deleteRating(@PathVariable(name = "author") String author, @PathVariable(name="userName") String userName){
        GeneralUser u = generalUserRepository.findGeneralUserByUserName(userName);
        if(u != null){
            List<Rating> lr =  ratingRepository.findRatingsByReviewee(u);
            Set<Rating> modified = new HashSet<>();
            Rating del = null;
            for(int i = 0; i < lr.size(); i++){
                if(lr.get(i).getAuthorUsername().equals(userName)){
                    del = lr.get(i);
                    continue;
                }
                modified.add(lr.get(i));
            }

            if(del != null){
                ratingRepository.delete(del);
                u.setMyRatings(modified);
                generalUserRepository.save(u);
                return "{\"serverResponse\" : true}";

            }

        }
        return "{\"serverResponse\" : false}";
    }

    @PostMapping("/modifyRating/{author}/{userName}")
    public String modifyRating(@PathVariable(name = "author") String author, @PathVariable(name="userName") String userName){
        GeneralUser u = generalUserRepository.findGeneralUserByUserName(userName);
        if(u != null){
            List<Rating> lr =  ratingRepository.findRatingsByReviewee(u);
            Set<Rating> modified = new HashSet<>();
            Rating del = null;

            for(int i = 0; i < lr.size(); i++){
                if(lr.get(i).getAuthorUsername().equals(userName)){
                    del = lr.get(i);
                    continue;
                }
                modified.add(lr.get(i));
            }

            if(del != null){
                ratingRepository.delete(del);
                u.setMyRatings(modified);
                generalUserRepository.save(u);
                return "{\"serverResponse\" : true}";

            }

        }
        return "{\"serverResponse\" : false}";
    }

    @GetMapping("getMyRatings/{userName}")
    public String getMyRatings(@PathVariable(name = "userName") String userName){
        GeneralUser u = generalUserRepository.findGeneralUserByUserName(userName);
        if(u != null){
            List<Rating> myratings = ratingRepository.findRatingsByAuthorUsername(u.getUserName());
            GsonBuilder builder = new GsonBuilder();
            builder.serializeNulls();
            Gson gson = builder.setPrettyPrinting().create();
            String json = gson.toJson(myratings);
            return "{\"ratings\" : " + json + "}";
        }
        return "{\"ratings\" : null}";
    }
}
