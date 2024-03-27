package mainPackage.meetingLocationPackage;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@TableGenerator(
        name = "meetingLocationGenerator",
        allocationSize = 1,
        initialValue = 1)
@Table(name = "meeting_locations")
public class MeetingLocation {

    @Column(name = "id")
    @Id
    @GeneratedValue(
            strategy=GenerationType.TABLE,
            generator="meetingLocationGenerator")
    private int id;

    @Column(name = "map_x")
    @NotBlank
    private String x;

    @Column(name = "map_y")
    @NotBlank
    private String y;

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public int getId() {
        return id;
    }


}
