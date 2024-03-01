package mainPackage.adminService;

import mainPackage.announcementPackage.Announcement;
import mainPackage.announcementPackage.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class adminController {

    @Autowired
    private AnnouncementRepository announcementRepository;

    // Create
    @PostMapping("/announcements/create")
    public String createAnnouncement(@RequestBody Announcement a) {
        a.setDate(new Date());
        announcementRepository.save(a);
        // Title: " + a.getTitle() + "\nDescription: " + a.getDescription();
        String response = "Announcement created.";
        return "{\"status\": \"" + response + "\"}";
    }

    // Read
    @GetMapping("/announcements/{id}")
    public Announcement readAnnouncement(@PathVariable(name = "id") int id) {
        return announcementRepository.findAnnouncementById(id);
    }

    // Update
    @PutMapping("/announcements/update/{id}")
    public String updateAnnouncement(@PathVariable("id") int id, @RequestBody Announcement a) {
        Announcement existingAnnouncement = announcementRepository.findAnnouncementById(id);

        if (existingAnnouncement == null) {
            return "Announcement with ID " + id + " does not exist.";
        }

        if (a != null) {
            // Update the existing announcement with the data from 'a'
            if (a.getTitle() != null) {
                existingAnnouncement.setTitle(a.getTitle());
            }

            if (a.getDescription() != null) {
                existingAnnouncement.setDescription(a.getDescription());
            }

            // Save the updated announcement
            announcementRepository.save(existingAnnouncement);

            return "Announcement with ID " + id + " updated successfully.";
        } else {
            return "Invalid announcement data provided.";
        }
    }


    // Delete
    @DeleteMapping("/announcements/del/{id}")
    public String deleteAnnouncement(@PathVariable(name = "id") int id) {
        Announcement a = announcementRepository.findAnnouncementById(id);
        if (a == null) {
            return "Announcement does not exist.";
        } else {
            announcementRepository.delete(a);
            return "Deleted " + a.getTitle() + " successfully.";
        }
    }

    // List
    @GetMapping("/announcements")
    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }
}
// testing