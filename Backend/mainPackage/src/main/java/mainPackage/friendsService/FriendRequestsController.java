package mainPackage.friendsService;

import ch.qos.logback.classic.Logger;
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
    public List<Friend> listFriendRequests(@PathVariable int id) {
        GeneralUser user = generalUserRepository.findGeneralUserById(id);

        if (user == null) {
            return Collections.emptyList();
        }

        return friendRepository.findFriendRequestsByReceiver(user);
    }

    @GetMapping("/potential-friends")
    public List<GeneralUser> listPotentialFriends(@PathVariable int id) {
        GeneralUser user = generalUserRepository.findGeneralUserById(id);

        if (user == null) {
            return Collections.emptyList();
        }

        return generalUserRepository.findPotentialFriends(id);
    }
}
