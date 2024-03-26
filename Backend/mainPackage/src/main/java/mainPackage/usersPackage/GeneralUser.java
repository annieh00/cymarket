package mainPackage.usersPackage;




import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import mainPackage.websocket.Auction;
import mainPackage.websocket.AuctionTable;
import mainPackage.websocket.Chat;
import org.hibernate.dialect.pagination.FetchLimitHandler;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Junhyung Shim
 * implementation of normal user
 * Watch out for getter and setter names, it might cause errors
 * */
@TableGenerator(
        name = "usersGenerator",
        allocationSize = 1,
        initialValue = 1)
@Entity
@Table(name="users")
public class GeneralUser {
    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "email",unique = true)
    private String email;

    @GeneratedValue(
            strategy=GenerationType.TABLE,
            generator="usersGenerator")
    @Id
    @Column(name = "uid")
    private int id;

    @Column(name = "password")
    private String password;

    @Column(name="userType")
    private int userType;

    @Column(name = "userName",unique = true)
    private String userName;


    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<AuctionTable> connectedSessions = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Chat> connectedChats = new HashSet<>();

    public Set<AuctionTable> getConnectedSessions() {
        return connectedSessions;
    }

    public void setConnectedSessions(Set<AuctionTable> connectedSessions) {
        this.connectedSessions = connectedSessions;
    }

    public void addConnectedSessions(AuctionTable a){
        this.connectedSessions.add(a);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }
}
