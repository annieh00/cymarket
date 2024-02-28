package mainPackage.announcementPackage;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@TableGenerator(
        name = "announcementsGenerator",
        allocationSize = 1,
        initialValue = 1)
@Table(name = "announcements")
public class Announcement {

    @Column(name = "id")
    @Id
    @GeneratedValue(
            strategy=GenerationType.TABLE,
            generator="announcementsGenerator")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "description")
    private String description;

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) { this.date = date; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
