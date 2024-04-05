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
import java.util.List;

@RestController
@RequestMapping("/friendrequests/{username}")
public class FriendRequestsController {

    @Autowired
    private GeneralUserRepository generalUserRepository;

    // Send a friend request
    @PostMapping("/send/{friendUsername}")
    @Operation(summary = "Send a friend request",
            description = "Sends a friend request to the specified friend.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend request sent successfully"),
            @ApiResponse(responseCode = "404", description = "User or friend not found")
    })
    public String sendFriendRequest(@PathVariable String username, @PathVariable String friendUsername) {
        // Retrieve the sending user from the database
        GeneralUser sendingUser = generalUserRepository.findGeneralUserByUserName(username);

        // Retrieve the receiving user (the friend) from the database
        GeneralUser receivingUser = generalUserRepository.findGeneralUserByUserName(friendUsername);

        // Check if either user is not found
        if (sendingUser == null || receivingUser == null) {
            return "{\"status\": \"User or friend not found.\"}";
        }

        // Add the receiving user to the sending user's list of friend requests
        sendingUser.getFriendRequests().add(receivingUser);

        // Save the changes to the sending user
        generalUserRepository.save(sendingUser);

        return "{\"status\": \"Friend request sent to " + friendUsername + " successfully.\"}";
    }

    // Accept a friend request
    @PostMapping("/accept/{requesterUsername}")
    @Operation(summary = "Accept a friend request",
            description = "Accepts a friend request from the specified requester.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend request accepted successfully"),
            @ApiResponse(responseCode = "404", description = "User or requester not found")
    })
    public String acceptFriendRequest(@PathVariable String username, @PathVariable String requesterUsername) {
        // Retrieve the accepting user from the database
        GeneralUser acceptingUser = generalUserRepository.findGeneralUserByUserName(username);

        // Retrieve the requester user from the database
        GeneralUser requesterUser = generalUserRepository.findGeneralUserByUserName(requesterUsername);

        // Check if either user is not found
        if (acceptingUser == null || requesterUser == null) {
            return "{\"status\": \"User or requester not found.\"}";
        }

        // Add the requester user to the accepting user's list of friends
        acceptingUser.getFriends().add(requesterUser);

        // Add the accepting user to the requester user's list of friends
        requesterUser.getFriends().add(acceptingUser);

        // Save the changes to both users
        generalUserRepository.save(acceptingUser);
        generalUserRepository.save(requesterUser);

        return "{\"status\": \"Friend request from " + requesterUsername + " accepted.\"}";
    }

    // Reject a friend request
    @PostMapping("/reject/{requesterUsername}")
    @Operation(summary = "Reject a friend request",
            description = "Rejects a friend request from the specified requester.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend request rejected successfully"),
            @ApiResponse(responseCode = "404", description = "User or requester not found")
    })
    public String rejectFriendRequest(@PathVariable String username, @PathVariable String requesterUsername) {
        // Retrieve the user who received the friend request
        GeneralUser receivingUser = generalUserRepository.findGeneralUserByUserName(username);

        // Retrieve the user who sent the friend request
        GeneralUser requesterUser = generalUserRepository.findGeneralUserByUserName(requesterUsername);

        // Check if either user is not found
        if (receivingUser == null || requesterUser == null) {
            return "{\"status\": \"User or requester not found.\"}";
        }

        // Remove the requester user from the receiving user's list of friend requests
        receivingUser.getFriendRequests().remove(requesterUser);

        // Save the changes to the receiving user
        generalUserRepository.save(receivingUser);

        return "{\"status\": \"Friend request from " + requesterUsername + " rejected successfully.\"}";
    }

    // List friend requests
    @GetMapping("/")
    @Operation(summary = "List friend requests",
            description = "Lists all pending friend requests for the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend requests listed successfully")
    })
    public List<GeneralUser> listFriendRequests(@PathVariable String username) {
        // Retrieve the user from the database
        GeneralUser user = generalUserRepository.findGeneralUserByUserName(username);

        // Check if the user exists
        if (user == null) {
            // If the user doesn't exist, return an empty list
            return new ArrayList<>();
        }

        // Return the list of friend requests for the user
        return new ArrayList<>(user.getFriendRequests());
    }

    @GetMapping("/potential-friends")
    @Operation(summary = "List potential friends",
            description = "Lists all users who are not the specified user and are neither in their friends list nor in their friend requests list.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Potential friends listed successfully")
    })
    public List<GeneralUser> listPotentialFriends(@PathVariable String username) {
        // Retrieve the user from the database
        GeneralUser user = generalUserRepository.findGeneralUserByUserName(username);

        // Check if the user exists
        if (user == null) {
            // If the user doesn't exist, return an empty list
            return new ArrayList<>();
        }

        // Retrieve all users from the database
        List<GeneralUser> allUsers = generalUserRepository.findAll();

        // Remove the specified user from the list of all users
        allUsers.remove(user);

        // Remove users who are in the user's friends list
        allUsers.removeAll(user.getFriends());

        // Remove users who are in the user's friend requests list
        allUsers.removeAll(user.getFriendRequests());

        return allUsers;
    }

}
