package mainPackage.websocket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import mainPackage.announcementPackage.Announcement;
import mainPackage.meetingLocationPackage.MeetingLocation;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chats")
@TableGenerator(
        name = "chatGenerator",
        allocationSize = 1,
        initialValue = 1)
public class Chat {

    @Id
    @GeneratedValue(
            strategy=GenerationType.TABLE,
            generator="chatGenerator")
    private int id;

    @Column
    private String username_1;
    @Column
    private String username_2;

    @OneToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Message> connectedMessages = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private MeetingLocation meetingLocation;
}
