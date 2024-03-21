package mainPackage.websocket;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import mainPackage.usersPackage.GeneralUser;
import org.springframework.context.annotation.Primary;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "Auction")
public class AuctionTable {
    @Id
    @Column(name = "aid")
    private String id;


    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<GeneralUser> connectedUsers;

    public AuctionTable(){
        connectedUsers =  new HashSet<>();
        id = null;
    }
    public AuctionTable(String id, GeneralUser user){
        connectedUsers =  new HashSet<>();
        getConnectedUsers().add(user);
        this.id = id;
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





}
