package mainPackage.announcementPackage;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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
    @NotBlank
    @Size(min = 1, max = 255)
    private String title;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "description")
    @NotBlank
    @Size(max = 1000)
    private String description;

    public long getId() {
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
