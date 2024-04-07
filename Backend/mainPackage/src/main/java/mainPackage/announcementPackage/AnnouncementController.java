package mainPackage.announcementPackage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import mainPackage.usersPackage.GeneralUser;
import mainPackage.usersPackage.GeneralUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/announcements")
public class AnnouncementController {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @GetMapping("/")
    @Operation(summary = "List all announcements",
            description = "Lists all announcements for the specified user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Announcements listed successfully")
    })
    public List<Announcement> listAllAnnouncements() {
        return announcementRepository.findAll();
    }

    @GetMapping("/{announcementId}")
    @Operation(summary = "Get announcement by ID",
            description = "Gets the announcement with the specified ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Announcement retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Announcement not found")
    })
    public Announcement getAnnouncementById(@PathVariable int announcementId) {
        return announcementRepository.findAnnouncementById(announcementId);
    }
}
