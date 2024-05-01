package mainPackage.searchService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import mainPackage.usersPackage.GeneralUser;
import mainPackage.usersPackage.GeneralUserRepository;
import mainPackage.usersPackage.Posting;
import mainPackage.usersPackage.PostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import java.util.*;

@RestController
public class SearchController {
    @Autowired
    private PostingRepository postingRepository;

    @Autowired
    private GeneralUserRepository generalUserRepository;

    @PostMapping("/search/{uid}")
    @Operation(summary = "Search users or posts and add to search history",
            description = "Search users or posts and add to search history with a given query.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "404", description = "Invalid user ID or other input error")
    })
    public ResponseEntity<String> search(String query, @PathVariable int uid) {
        GeneralUser user = generalUserRepository.findGeneralUserById(uid);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"error\": \"User not found\" }");
        }

        Set<Object> results = new HashSet<>();

        results.addAll(generalUserRepository.findByUserNameContainingIgnoreCase(query));
        results.addAll(generalUserRepository.findByFirstNameContainingIgnoreCase(query));
        results.addAll(generalUserRepository.findByLastNameContainingIgnoreCase(query));
        results.addAll(postingRepository.findByUserNameContainingIgnoreCase(query));
        results.addAll(postingRepository.findByTitleContainingIgnoreCase(query));
        results.addAll(postingRepository.findByDescriptionContainingIgnoreCase(query));
        results.addAll(postingRepository.findByCategory(query));
        results.addAll(postingRepository.findByCategoriesContaining(query));

        // Add query to search history
        user.getSearchHistory().add(query);
        generalUserRepository.save(user);

        // Process the results to add Base64-encoded image data for `picture1`
        List<String> resultsWithImages = new ArrayList<>();
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

        for (Object result : results) {
            if (result instanceof Posting) {
                Posting post = (Posting) result;

                String postJson = gson.toJson(post);
                String picture1 = post.getPicture1();

                if (picture1 != null && !picture1.isEmpty()) {
                    File imageFile = new File("images/" + picture1);

                    if (imageFile.exists()) {
                        try {
                            byte[] fileContent = FileUtils.readFileToByteArray(imageFile);
                            String encodedImage = Base64.getEncoder().encodeToString(fileContent);

                            // Insert Base64-encoded image data
                            postJson = postJson.substring(0, postJson.length() - 1); // Remove closing brace
                            postJson += ", \"picture1Data\": \"" + encodedImage + "\"}"; // Append Base64 image data
                        } catch (IOException e) {
                            e.printStackTrace(); // Handle error
                        }
                    }
                }

                resultsWithImages.add(postJson); // Store modified JSON
            } else {
                resultsWithImages.add(gson.toJson(result)); // Add other results
            }
        }

        // Create the final JSON response
        String jsonResponse = "{ \"results\": [" + String.join(", ", resultsWithImages) + "] }";

        return ResponseEntity.ok(jsonResponse);
    }

    @Operation(summary = "Get a user's search history",
            description = "Retrieves all entries from a user's search history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved search history"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/search/{uid}/history")
    public ResponseEntity<List<String>> getSearchHistory(@PathVariable int uid) {
        GeneralUser user = generalUserRepository.findGeneralUserById(uid);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<String> searchHistory = user.getSearchHistory();

        return ResponseEntity.ok(searchHistory);
    }

    @Operation(summary = "Clear a user's search history",
            description = "Clears all entries from a user's search history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully cleared the search history"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/search/{uid}/clear")
    @Transactional
    public ResponseEntity<String> clearSearchHistory(@PathVariable int uid) {
        GeneralUser user = generalUserRepository.findGeneralUserById(uid);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        user.getSearchHistory().clear();
        generalUserRepository.save(user);

        return ResponseEntity.ok("Search history cleared");
    }

}
