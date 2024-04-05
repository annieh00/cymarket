package mainPackage.websocket;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import mainPackage.announcementPackage.Announcement;
import mainPackage.usersPackage.GeneralUser;
import mainPackage.usersPackage.Posting;

import org.springframework.context.annotation.Primary;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "Auction")
public class AuctionTable {

    @Id
    @Column(name = "url")
    private String id;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "postId")
    private Posting post;

    //@Column(name = "highestBidderUid")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "uid")
    private GeneralUser highestBidder;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<GeneralUser> connectedUsers;


    @Column(name = "HighestBidAmount")
    private int highestBidAmount;


    @JsonIgnore
    @Lob
    private String bidHistory;



    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Message winnerChat;


    public Message getWinnerChat() {
        return winnerChat;
    }

    public void setWinnerChat(Message winnerChat) {
        this.winnerChat = winnerChat;
    }


    public AuctionTable(){
        connectedUsers =  new HashSet<>();
        id = null;
        bidHistory = "";
    }
    public AuctionTable(String id, GeneralUser user){
        connectedUsers =  new HashSet<>();
        getConnectedUsers().add(user);
        this.id = id;
        bidHistory = "";
    }
    public Set<GeneralUser> getConnectedUsers() {
        return connectedUsers;
    }

    public void setConnectedUsers(Set<GeneralUser> connectedUsers) {
        this.connectedUsers = connectedUsers;
    }

    public void addConnectedUsers(GeneralUser u){
        this.connectedUsers.add(u);
    }

    public String getId() {
        return id;
    }

    public void setId(String id){this.id = id;}

    public Posting getPost() {
        return post;
    }

    public void setPost(Posting post) {
        this.post = post;
    }


    public GeneralUser getHighestBidder() {
        return highestBidder;
    }

    public void setHighestBidder(GeneralUser user) {
        this.highestBidder = user;
    }

    public String getBidHistory() {
        return bidHistory;
    }

    public void setBidHistory(String bidHistory) {
        this.bidHistory = bidHistory;
    }

    public int getHighestBidAmount() {
        return highestBidAmount;
    }

    public void setHighestBidAmount(int highestBidAmount) {
        this.highestBidAmount = highestBidAmount;
    }



}
