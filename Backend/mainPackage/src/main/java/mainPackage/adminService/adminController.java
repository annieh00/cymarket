package mainPackage.adminService;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Create a new announcement",
            description = "Creates a new announcement and saves it to the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Announcement created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid announcement data provided")
    })
    public String createAnnouncement(@RequestBody Announcement a) {
        a.setDate(new Date());
        announcementRepository.save(a);
        String response = "Announcement created.";
        return "{\"status\": \"" + response + "\"" + ", \"id\" : \"" + a.getId() + "\"}";
    }

    // Update
    @PutMapping("/announcements/update/{id}")
    @Operation(summary = "Update an announcement",
            description = "Updates an existing announcement with the provided data in the request body.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Announcement updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid announcement data provided"),
            @ApiResponse(responseCode = "404", description = "Announcement not found")
    })

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
            String response = "Announcement updated.";
            return "{\"status\": \"" + response + "\"}";
        } else {
            String response = "Announcement failed to update.";
            return "{\"status\": \"" + response + "\"}";
        }
    }

    // Delete
    @DeleteMapping("/announcements/del/{id}")
    @Operation(summary = "Delete an announcement",
            description = "Deletes an announcement based on the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Announcement deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Announcement not found")
    })
    public String deleteAnnouncement(@PathVariable(name = "id") int id) {
        Announcement a = announcementRepository.findAnnouncementById(id);
        if (a == null) {
            String response = "Announcement is null.";
            return "{\"status\": \"" + response + "\"}";
        } else {
            announcementRepository.delete(a);
            String response = "Announcement deleted successfully.";
            return "{\"status\": \"" + response + "\"}";
        }
    }

}
