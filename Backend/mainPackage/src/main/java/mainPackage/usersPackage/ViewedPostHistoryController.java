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

import java.util.List;

@RestController
@RequestMapping("/posts")
public class ViewedPostHistoryController {
    @Autowired
    GeneralUserRepository generalUserRepository;

    @Autowired
    PostingRepository postingRepository;

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

        if (!u.getViewedPostHistory().contains(p)) {
            u.getViewedPostHistory().add(p);
        }
        if (!p.getViewedUsers().contains(u)) {
            p.getViewedUsers().add(u);
        }

        generalUserRepository.save(u);
        postingRepository.save(p);

        return ResponseEntity.ok("Post view recorded");
    }

    @Operation(summary = "Get a user's viewed post history", description = "Fetches the posts a user has recently viewed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the viewed post history", content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = Posting.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{uid}/view_history")
    public ResponseEntity<List<Posting>> findRecentlyViewedPosts(@PathVariable int uid) {
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
    @DeleteMapping("/{uid}/view_history/delete/{pid}")
    public ResponseEntity<String> deletePostFromViewHistory(@PathVariable int uid, @PathVariable int pid) {
        GeneralUser u = generalUserRepository.findGeneralUserById(uid);

        if (u == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        List<Posting> viewedPosts = u.getViewedPostHistory();

        boolean removed = viewedPosts.removeIf(post -> post.getId() == pid);

        if (removed) {
            generalUserRepository.save(u);
            return ResponseEntity.ok("Post removed from view history");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found in view history");
        }
    }
}
