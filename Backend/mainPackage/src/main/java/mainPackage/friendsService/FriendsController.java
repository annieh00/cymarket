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
    @RequestMapping("/friends/{id}")
    public class FriendsController {

        @Autowired
        private GeneralUserRepository generalUserRepository;

        @Autowired
        private FriendRepository friendRepository;

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
        @DeleteMapping("/del/{uid}")
        @Operation(summary = "Remove a friend",
                description = "Removes a friend based on the provided ID.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Friend removed successfully"),
                @ApiResponse(responseCode = "404", description = "Friend not found")
        })
        public String removeFriend(@PathVariable int uid, @PathVariable int id) {
            GeneralUser u = generalUserRepository.findGeneralUserById(uid);
            GeneralUser friendToRemove = generalUserRepository.findGeneralUserById(id);

            if (u == null || friendToRemove == null) {
                return "Friend or user does not exist.";
            } else {
                Friend friend = friendRepository.findFriendBySenderAndReceiver(u, friendToRemove);
                if (friend != null) {
                    friendRepository.delete(friend);
                    return "Removed " + friendToRemove.getUserName() + " successfully.";
                } else {
                    return friendToRemove.getUserName() + " is not a friend.";
                }
            }
        }

        // List
        @GetMapping("/list")
        @Operation(summary = "Get all friends", description = "Returns a list of all friends")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Friends retrieved successfully")
        })
        public List<GeneralUser> getAllFriends(@PathVariable int id) {
            GeneralUser u = generalUserRepository.findGeneralUserById(id);
            if (u == null) {
                return Collections.emptyList();
            }

            List<GeneralUser> friends = new ArrayList<>();
            List<Friend> userFriends = friendRepository.findFriendRequestsByReceiver(u);
            for (Friend friend : userFriends) {
                friends.add(friend.getSender());
            }
            return friends;
        }
    }
