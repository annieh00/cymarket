package mainPackage.friendsService;

import jakarta.persistence.*;
import mainPackage.usersPackage.GeneralUser;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@TableGenerator(
        name = "friendsGenerator",
        allocationSize = 1,
        initialValue = 1)
public class Friend {

    @Id
    @GeneratedValue(
            strategy=GenerationType.TABLE,
            generator="friendsGenerator")
    private Long id;

    @OneToOne
    private GeneralUser sender; // User who sent the friend request

    @OneToOne
    private GeneralUser receiver; // User who received the friend request

    public enum FriendshipStatus {
        PENDING,  // User A sent a friend request to User B, waiting for response
        ACCEPTED, // User A and User B are friends
        DECLINED, // User B declined User A's friend request
        BLOCKED,  // User A or User B blocked the other
        BROKEN    // Friendship was previously accepted but is now broken
    }

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status; // Status of the friendship (pending, accepted, etc.)

    @CreatedDate
    private LocalDateTime createdAt;

    public Friend(GeneralUser sender, GeneralUser receiver, FriendshipStatus status) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public Friend() {}

    public GeneralUser getSender() { return sender; }

    public void setInitiator(GeneralUser sender) { this.sender = sender; }

    public FriendshipStatus getStatus() { return this.status; }

    public void setStatus(FriendshipStatus friendshipStatus) { this.status = status; }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }
}
