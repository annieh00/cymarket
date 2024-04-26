package mainPackage.searchService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import mainPackage.usersPackage.GeneralUser;
import mainPackage.usersPackage.GeneralUserRepository;
import mainPackage.usersPackage.Posting;
import mainPackage.usersPackage.PostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class SearchController {
    @Autowired
    private PostingRepository postingRepository;

    @Autowired
    private GeneralUserRepository generalUserRepository;

    @GetMapping("/search/{uid}")
    @Operation(summary = "Search users or posts and adds to search history",
            description = "Search users or posts and adds to search history through a search of String type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Invalid input")
    })
    public String search(String query, @PathVariable int uid) {
        Set<Object> results = new HashSet<>();

        GeneralUser u = generalUserRepository.findGeneralUserById(uid);

        results.addAll(generalUserRepository.findByUserNameContainingIgnoreCase(query));
        results.addAll(generalUserRepository.findByFirstNameContainingIgnoreCase(query));
        results.addAll(generalUserRepository.findByLastNameContainingIgnoreCase(query));
//        results.addAll(postingRepository.findByUserNameContainingIgnoreCase(query));
//        results.addAll(postingRepository.findByTitleContainingIgnoreCase(query));
//        results.addAll(postingRepository.findByDescriptionContainingIgnoreCase(query));
//        results.addAll(postingRepository.findByCategory(query));

        u.getSearchHistory().add(query);
        generalUserRepository.save(u);

        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(results);
        System.out.println("saving: " + query);
        return json;
    }

}
