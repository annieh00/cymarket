package mainPackage.upgradePrivilege;


import com.google.gson.annotations.Expose;
import jakarta.persistence.*;

@TableGenerator(
        name = "upgradeQueueGenerator",
        allocationSize = 1,
        initialValue = 1)
@Entity
@Table(name="UpgradeQueue")
public class UpgradeQueue {


    @Expose
    @GeneratedValue(
            strategy= GenerationType.TABLE,
            generator="upgradeQueueGenerator")
    @Id
    @Column(name = "qid")
    private int id;

    @Expose
    @Column(name = "userName", unique = true)
    private String userName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



}
