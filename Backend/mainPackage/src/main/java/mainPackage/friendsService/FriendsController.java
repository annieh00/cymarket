    package mainPackage.friendsService;


    import io.swagger.v3.oas.annotations.Operation;
    import io.swagger.v3.oas.annotations.responses.ApiResponse;
    import io.swagger.v3.oas.annotations.responses.ApiResponses;
    import mainPackage.usersPackage.GeneralUser;
    import mainPackage.usersPackage.GeneralUserRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.Date;
    import java.util.List;

    @RestController
    @RequestMapping("/friends/{username}")
    public class FriendsController {

        @Autowired
        private GeneralUserRepository generalUserRepository;

        // Read
        @GetMapping("/")
        @Operation(summary = "Find a friend by ID",
                description = "Returns a friend based on the provided ID.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Friend retrieved successfully"),
                @ApiResponse(responseCode = "404", description = "Friend not found")
        })
        public GeneralUser findFriendById(@PathVariable(name = "id") int id) {
            return generalUserRepository.findGeneralUserById(id);
        }

        // Delete
        @DeleteMapping("/del/{id}")
        @Operation(summary = "Remove a friend",
                description = "Removes a friend based on the provided ID.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Friend removed successfully"),
                @ApiResponse(responseCode = "404", description = "Friend not found")
        })
        public String removeFriend(@PathVariable(name = "id") int id) {
            GeneralUser u = generalUserRepository.findGeneralUserById(id);
            if (u == null) {
                return "Friend does not exist.";
            } else {
                generalUserRepository.deleteFriend(id);
                return "Removed" + u.getUserName() + " successfully.";
            }
        }

        // List
        @GetMapping("/list")
        @Operation(summary = "Get all friends", description = "Returns a list of all friends")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Friends retrieved successfully")
        })
        public List<GeneralUser> getAllFriends(String username) {
            GeneralUser u = generalUserRepository.findGeneralUserByUserName(username);
            if (u == null) {
                // Handle the case where the user with the specified username is not found
                return Collections.emptyList();
            }
            return new ArrayList<>(u.getFriends());
        }
    }
