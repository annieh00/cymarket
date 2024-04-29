package mainPackage.usersPackage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Book;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookmarks")
public class BookmarkPostController {

    @Autowired
    private GeneralUserRepository generalUserRepository;

    @Autowired
    private PostingRepository postingRepository;

    @Autowired BookmarkRepository bookmarkRepository;

    @Operation(summary = "Add a bookmark", description = "Adds a post to a user's bookmarks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bookmark added"),
            @ApiResponse(responseCode = "404", description = "User or post not found")
    })
    @PostMapping("/{uid}/add/{pid}")
    @Transactional
    public ResponseEntity<String> addBookmark(@PathVariable int uid, @PathVariable int pid) {
        GeneralUser user = generalUserRepository.findGeneralUserById(uid);
        Posting post = postingRepository.findPostingById(pid);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        Bookmark bookmark = new Bookmark();
        bookmark.setPost(post);
        bookmark.setUser(user);
        bookmarkRepository.save(bookmark);

        return ResponseEntity.ok("Bookmark added");
    }

    @Operation(summary = "Remove a bookmark", description = "Removes a post from a user's bookmarks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bookmark removed"),
            @ApiResponse(responseCode = "404", description = "User or post not found")
    })
    @DeleteMapping("/{uid}/remove/{pid}")
    @Transactional
    public ResponseEntity<String> removeBookmark(@PathVariable int uid, @PathVariable int pid) {
        GeneralUser user = generalUserRepository.findGeneralUserById(uid);
        Posting post = postingRepository.findPostingById(pid);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        Bookmark bookmark = bookmarkRepository.findByUserAndPost(user, post);

        if (bookmark == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bookmark not found");
        }

        bookmarkRepository.delete(bookmark);

        return ResponseEntity.ok("Bookmark removed");
    }

    @Operation(summary = "Get all bookmarks for a user", description = "Fetches all posts bookmarked by a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved bookmarks", content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = Posting.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{uid}")
    public ResponseEntity<List<Posting>> getBookmarks(@PathVariable int uid) {
        GeneralUser user = generalUserRepository.findGeneralUserById(uid);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<Bookmark> bookmarks = bookmarkRepository.findAllByUser(user);

        List<Posting> bookmarkedPosts = bookmarks.stream()
                .map(Bookmark::getPost)
                .collect(Collectors.toList());

        return ResponseEntity.ok(bookmarkedPosts);
    }
}
