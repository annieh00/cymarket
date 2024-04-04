package mainPackage.websocket;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.Data;
import lombok.Getter;
import mainPackage.usersPackage.GeneralUser;

@Entity
@Table(name = "messages")
@TableGenerator(
        name = "messageGenerator",
        allocationSize = 1,
        initialValue = 1)
@Data
public class Message {
    @Id
    @GeneratedValue(
            strategy=GenerationType.TABLE,
            generator="messageGenerator")
    private Long id;

    @Lob
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent")
    private Date sent = new Date();

    @JsonIgnore
    @ManyToOne
    private GeneralUser userSent;

    @JsonIgnore
    @ManyToOne
    private GeneralUser userReceived;

    public Message() {};

    public Message(GeneralUser userSent, String content) {
        this.userSent = userSent;
        this.content = content;
        this.userReceived = null;
    }

    public Message(GeneralUser userSent, GeneralUser userReceived, String content) {
        this.userSent = userSent;
        this.content = content;
        this.userReceived = userReceived;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    public GeneralUser getUserSent() { return userSent; }

    public void setUserSent(GeneralUser userSent) { this.userSent = userSent; }

    public GeneralUser getUserReceived() { return userReceived; }

    public void setUserReceived(GeneralUser userReceived) { this.userReceived = userReceived; }

}

