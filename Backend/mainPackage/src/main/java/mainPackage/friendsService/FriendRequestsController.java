package mainPackage.friendsService;

import ch.qos.logback.classic.Logger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import mainPackage.usersPackage.GeneralUser;
import mainPackage.usersPackage.GeneralUserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/friendrequests/{id}")
public class FriendRequestsController {

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private GeneralUserRepository generalUserRepository;

    @PostMapping("/send/{friendId}")
    @Operation(summary = "Send friend request",
            description = "Send a friend request from the user with the specified ID to the friend with the specified ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend request sent successfully"),
            @ApiResponse(responseCode = "404", description = "User or friend not found"),
            @ApiResponse(responseCode = "409", description = "A pending friend request already exists")
    })
    public String sendFriendRequest(@PathVariable int id, @PathVariable int friendId) {
        GeneralUser sender = generalUserRepository.findGeneralUserById(id);
        GeneralUser receiver = generalUserRepository.findGeneralUserById(friendId);
        if (sender == null || receiver == null) {
            return "User or friend not found.";
        }
        // Check if there's already a pending friend request
        Friend existingRequest = friendRepository.findPendingRequest(id, friendId);
        if (existingRequest != null) {
            return "A pending friend request already exists.";
        }
        // Create and save the new friend request
        Friend friendRequest = new Friend(sender, receiver, Friend.FriendshipStatus.PENDING);
        friendRepository.save(friendRequest);
        return "Friend request sent to " + receiver.getUserName() + " successfully.";
    }

    @PostMapping("/accept/{friendId}")
    @Operation(summary = "Accept friend request",
            description = "Accept a pending friend request from the user with the specified ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend request accepted successfully"),
            @ApiResponse(responseCode = "404", description = "Friend request not found or already accepted")
    })
    public String acceptFriendRequest(@PathVariable int id, @PathVariable int friendId) {
        GeneralUser user = generalUserRepository.findGeneralUserById(id);
        GeneralUser friend = generalUserRepository.findGeneralUserById(friendId);
        Friend friendRequest = friendRepository.findFriendBySenderAndReceiver(friend, user);

        if (friendRequest == null || friendRequest.getStatus() != Friend.FriendshipStatus.PENDING) {
            return "Friend request not found or already accepted.";
        }

        friendRequest.setStatus(Friend.FriendshipStatus.ACCEPTED);
        friendRepository.save(friendRequest);

        return "Friend request from " + friendRequest.getSender().getUserName() + " accepted.";
    }

    @PostMapping("/reject/{requesterId}")
    @Operation(summary = "Reject friend request",
            description = "Reject a pending friend request from the user with the specified ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend request rejected successfully"),
            @ApiResponse(responseCode = "404", description = "Friend request not found or already rejected")
    })
    public String rejectFriendRequest(@PathVariable int id, @PathVariable int requesterId) {
        GeneralUser user = generalUserRepository.findGeneralUserById(id);
        GeneralUser requester = generalUserRepository.findGeneralUserById(requesterId);
        Friend friendRequest = friendRepository.findFriendBySenderAndReceiver(requester, user);

        if (friendRequest == null || friendRequest.getStatus() != Friend.FriendshipStatus.PENDING) {
            return "Friend request not found or already rejected.";
        }

        friendRepository.delete(friendRequest);

        return "Friend request from " + friendRequest.getSender().getUserName() + " rejected successfully.";
    }

    @GetMapping("/")
    @Operation(summary = "List friend requests",
            description = "List all pending friend requests for the user with the specified ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of friend requests retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public List<Friend> listFriendRequests(@PathVariable int id) {
        GeneralUser user = generalUserRepository.findGeneralUserById(id);

        if (user == null) {
            return Collections.emptyList();
        }

        return friendRepository.findFriendRequestsByReceiver(user);
    }

    @GetMapping("/potential-friends")
    @Operation(summary = "List potential friends",
            description = "List all potential friends for the user with the specified ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of potential friends retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public List<GeneralUser> listPotentialFriends(@PathVariable int id) {
        GeneralUser user = generalUserRepository.findGeneralUserById(id);

        if (user == null) {
            return Collections.emptyList();
        }

        return generalUserRepository.findPotentialFriends(id);
    }
}
