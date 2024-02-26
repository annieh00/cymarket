package mainPackage.usersPackage;




import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

/**
 * @author Junhyung Shim
 * implementation of normal user
 * Watch out for getter and setter names, it might cause errors
 * */

@Entity
@Table(name="users")
public class GeneralUser {
    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;


    @Column(name = "email",unique = true)
    private String email;

    @Id
    @Column(name = "uid")
    private int id;

    @Column(name = "password")
    private String password;

    @Column(name="userType")
    private int userType;

    @Column(name = "userName",unique = true)
    private String userName;


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
