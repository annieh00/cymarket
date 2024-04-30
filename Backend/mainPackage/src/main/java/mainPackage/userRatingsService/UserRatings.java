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

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserRatings {

    @Autowired
    private GeneralUserRepository generalUserRepository;

    @Autowired
    private RatingRepository ratingRepository;
    @Operation(summary = "updates the user's rating", description = "user rating is between 0 to 5")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "user rating was set successfully", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "invalid input")
    })
    //create
    @PostMapping("/setUserRating/{userName}")
    public String setUserRating(@PathVariable("userName") String userName, @RequestBody Rating r){
        //userName is the person getting reviewed
        GeneralUser u = generalUserRepository.findGeneralUserByUserName(userName);
        if(u == null){
            return "{\"serverResponse\":false}";
        }
        if(r.getDescription() != null && !r.getDescription().isEmpty()){
            r.setRevieweeUserName(u.getUserName());
            ratingRepository.save(r);
            List<Rating> setOfRatings = u.getMyRatings();
            setOfRatings.add(r);

            double avg = 0.0;
            for(Rating r2 : setOfRatings){
                avg += r2.getStars();
            }
            if(setOfRatings.size() == 0){
                u.setScore(0.0);
            }else{
                u.setScore(avg / setOfRatings.size());
            }

            generalUserRepository.save(u);
            System.out.println("Review::::" + r.getRevieweeUserName());
            return "{\"serverResponse\":true}";
        }

        return "{\"serverResponse\":false}";
    }

    @GetMapping("/getAllRatings")
    public String getAllRatings(){
        List<Rating> lr = ratingRepository.findAll();

        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(lr);
        return "{\"ratings\":" + json + "}";
    }

    @GetMapping("/getRatingsOf/{userName}")
    public String getRatings(@PathVariable(name = "userName") String userName){
        GeneralUser u = generalUserRepository.findGeneralUserByUserName(userName);
        if(u != null){
            List<Rating> lr =  ratingRepository.findRatingsByRevieweeUserName(u.getUserName());
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
            List<Rating> lr =  ratingRepository.findRatingsByRevieweeUserName(u.getUserName());
            List<Rating> modified = new ArrayList<>();
            Rating del = null;
            double sum = 0.0;
            for(int i = 0; i < lr.size(); i++){
                if(lr.get(i).getAuthorUsername().equals(userName)){
                    del = lr.get(i);
                    continue;
                }
                sum += lr.get(i).getStars();
                modified.add(lr.get(i));
            }

            if(del != null){
                ratingRepository.delete(del);
                if(modified.size() == 0){
                    u.setScore(0);
                }else{
                    u.setScore(sum/lr.size());
                }
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
