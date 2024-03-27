package mainPackage.websocket;

import java.util.Date;

import jakarta.persistence.*;

import lombok.Data;

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

    @Column(name = "user_name")
    private String userName;

    @Lob
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent")
    private Date sent = new Date();

    public Message() {};

    public Message(String userName, String content) {
        this.userName = userName;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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


}

