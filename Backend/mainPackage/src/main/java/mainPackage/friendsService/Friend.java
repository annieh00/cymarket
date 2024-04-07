package mainPackage.friendsService;

import jakarta.persistence.*;
import mainPackage.usersPackage.GeneralUser;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@TableGenerator(
        name = "friendGenerator",
        allocationSize = 1,
        initialValue = 1)
@Table(name = "friend")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "friendGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "uid")
    private GeneralUser sender; // User who sent the friend request

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "uid")
    private GeneralUser receiver; // User who received the friend request

    public Friend() {}

    public enum FriendshipStatus {
        PENDING,  // User A sent a friend request to User B, waiting for response
        ACCEPTED, // User A and User B are friends
        DECLINED, // User B declined User A's friend request
        BLOCKED,  // User A or User B blocked the other
        BROKEN    // Friendship was previously accepted but is now broken
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FriendshipStatus status; // Status of the friendship (pending, accepted, etc.)

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created")
    private LocalDateTime createdAt;

    public Friend(GeneralUser sender, GeneralUser receiver, FriendshipStatus status) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public GeneralUser getSender() { return sender; }

    public void setSender(GeneralUser sender) { this.sender = sender; }

    public GeneralUser getReceiver() { return receiver; }

    public void setReceiver(GeneralUser receiver) { this.receiver = receiver; }

    public FriendshipStatus getStatus() { return this.status; }

    public void setStatus(FriendshipStatus status) { this.status = status; }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public LocalDateTime getCreatedAt() { return this.createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
