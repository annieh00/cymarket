package mainPackage.usersPackage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

        System.out.println("\nBEFORE USER HISTORY: " + u.getViewedPostHistory() + "\n");

        ViewedPostHistory viewedPost = new ViewedPostHistory();
        viewedPost.setPost(p);
        viewedPost.setUser(u);
        viewedPostRepository.save(viewedPost);

        System.out.println("\nAFTER USER HISTORY: " + u.getViewedPostHistory() + "\n");

        return ResponseEntity.ok("Post view recorded");
    }

    @Operation(summary = "Get a user's viewed post history", description = "Fetches the posts a user has recently viewed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the viewed post history", content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = Posting.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{uid}/view_history")
    public ResponseEntity<List<ViewedPostHistory>> findRecentlyViewedPosts(@PathVariable int uid) {
        GeneralUser u = generalUserRepository.findGeneralUserById(uid);
        if (u == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(u.getViewedPostHistory());
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

        List<ViewedPostHistory> viewedPosts = u.getViewedPostHistory();
        
        ViewedPostHistory targetPost = null;
        for (ViewedPostHistory vph : viewedPosts) {
            if (vph.getId() == vpid) { 
                targetPost = vph;
                break;
            }
        }

        if (targetPost == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Viewed post not found");
        }


        viewedPosts.remove(targetPost);
        viewedPostRepository.delete(targetPost);

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

        List<ViewedPostHistory> viewedPosts = u.getViewedPostHistory();

        viewedPostRepository.deleteAll(viewedPosts);

        return ResponseEntity.ok("Viewed post history cleared");
    }

}
