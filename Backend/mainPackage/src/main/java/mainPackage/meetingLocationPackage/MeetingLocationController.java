package mainPackage.meetingLocationPackage;

import mainPackage.meetingLocationPackage.MeetingLocation;
import mainPackage.meetingLocationPackage.MeetingLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class MeetingLocationController {
    
    @Autowired
    private MeetingLocationRepository meetingLocationRepository;

    // Create
    @PostMapping("/meetinglocation/create")
    public String createMeetingLocation(@RequestBody MeetingLocation m) {
        meetingLocationRepository.save(m);
        String response = "Meeting location created.";
        return "{\"status\": \"" + response + "\"}";
    }

    // Read
    @GetMapping("/meetinglocation/{id}")
    public MeetingLocation readMeetingLocation(@PathVariable(name = "id") int id) {
        return meetingLocationRepository.findMeetingLocationById(id);
    }

    // Update
    @PutMapping("/meetinglocation/update/{id}")
    public String updateMeetingLocation(@PathVariable("id") int id, @RequestBody MeetingLocation m) {
        MeetingLocation existingMeetingLocation = meetingLocationRepository.findMeetingLocationById(id);

        if (existingMeetingLocation == null) {
            return "Meeting location with ID " + id + " does not exist.";
        }

        if (m != null) {
            // Update the existing MeetingLocation with the data from 'm'
            if (!m.getX().equals(existingMeetingLocation.getX())) {
                existingMeetingLocation.setX(m.getX());
            }

            if (!m.getY().equals(existingMeetingLocation.getY())) {
                existingMeetingLocation.setY(m.getY());
            }

            // Save the updated MeetingLocation
            meetingLocationRepository.save(existingMeetingLocation);

            return "Meeting location with ID " + id + " updated successfully.";
        } else {
            return "Invalid meeting location data provided.";
        }
    }


    // Delete
    @DeleteMapping("/meetinglocation/del/{id}")
    public String deleteMeetingLocation(@PathVariable(name = "id") int id) {
        MeetingLocation m = meetingLocationRepository.findMeetingLocationById(id);
        if (m == null) {
            return "Meeting location does not exist.";
        } else {
            meetingLocationRepository.delete(m);
            return "Deleted " + m.getId() + " successfully.";
        }
    }

    // List
    @GetMapping("/meetinglocation")
    public List<MeetingLocation> getAllMeetingLocations() {
        return meetingLocationRepository.findAll();
    }
}
