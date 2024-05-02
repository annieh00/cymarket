package mainPackage.usersPackage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class ViewedPostHistoryController {
    @Autowired
    GeneralUserRepository generalUserRepository;

    @Autowired
    PostingRepository postingRepository;

    @Autowired
    ViewedPostRepository viewedPostRepository;

    @Operation(summary = "Record a user viewing a post", description = "Records that a specific user viewed a specific post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully recorded the post view"),
            @ApiResponse(responseCode = "400", description = "Invalid user or post ID")
    })
    @PostMapping("/{uid}/view/{pid}")
    @Transactional
    public ResponseEntity<String> recordPostView(@PathVariable int uid, @PathVariable int pid) {
        GeneralUser u = generalUserRepository.findGeneralUserById(uid);
        Posting p = postingRepository.findPostingById(pid);

        if (u == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found with id: " + uid);
        }
        if (p == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post not found with id: " + pid);
        }

        ViewedPostHistory viewedPost = new ViewedPostHistory();
        viewedPost.setPost(p);
        viewedPost.setUser(u);
        viewedPostRepository.save(viewedPost);

        return ResponseEntity.ok("Post view recorded");
    }

    @Operation(summary = "Get a user's viewed post history", description = "Fetches the posts a user has recently viewed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the viewed post history", content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = Posting.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{uid}/view_history")
    public ResponseEntity<String> findRecentlyViewedPosts(@PathVariable int uid) {
        GeneralUser user = generalUserRepository.findGeneralUserById(uid);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"error\": \"User not found\" }");
        }

        List<ViewedPostHistory> viewedPostHistories = viewedPostRepository.findAllByUser(user);

        List<Posting> viewedPosts = viewedPostHistories.stream()
                .map(ViewedPostHistory::getPost)
                .collect(Collectors.toList());

        List<String> postsWithImages = new ArrayList<>();
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

        // Add Base64-encoded image data for `picture1` to each `Posting`
        try {
            for (Posting post : viewedPosts) {
                String postJson = gson.toJson(post);

                String picture1 = post.getPicture1();

                if (picture1 != null && !picture1.isEmpty()) {
                    File imageFile = new File("images/" + picture1);
                    if (imageFile.exists()) {
                        byte[] fileContent = FileUtils.readFileToByteArray(imageFile);
                        String encodedImage = Base64.getEncoder().encodeToString(fileContent);

                        // Insert the Base64 data into the JSON representation
                        postJson = postJson.substring(0, postJson.length() - 1); // Remove closing brace
                        postJson += ", \"picture1Data\": \"" + encodedImage + "\"}"; // Append Base64 image data
                    }
                }

                postsWithImages.add(postJson);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle error
        }

        String json = "{ \"viewed_posts\": [" + String.join(", ", postsWithImages) + "] }";

        return ResponseEntity.ok(json); // Return the JSON response with Base64-encoded image data
    }

    @Operation(summary = "Delete a post from a user's viewed history", description = "Deletes a specific post from a user's viewed post history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the post from the view history"),
            @ApiResponse(responseCode = "404", description = "User or post not found")
    })
    @DeleteMapping("/{uid}/view_history/delete/{vpid}")
    public ResponseEntity<String> deletePostFromViewHistory(@PathVariable int uid, @PathVariable int vpid) {
        GeneralUser u = generalUserRepository.findGeneralUserById(uid);

        if (u == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        ViewedPostHistory viewedPost = viewedPostRepository.findViewedPostById(vpid);

        if (viewedPost == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Viewed post not found");
        }

        viewedPostRepository.delete(viewedPost);

        return ResponseEntity.ok("Post removed from view history");
    }

    @Operation(summary = "Clear a user's viewed post history", description = "Clears all posts from a user's viewed post history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully cleared the viewed post history"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{uid}/view_history/clear")
    @Transactional
    public ResponseEntity<String> clearViewHistory(@PathVariable int uid) {
        GeneralUser u = generalUserRepository.findGeneralUserById(uid);

        if (u == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        List<ViewedPostHistory> viewedPostHistory = viewedPostRepository.findAllByUser(u);

        viewedPostRepository.deleteAll(viewedPostHistory);

        return ResponseEntity.ok("Viewed post history cleared");
    }

}
