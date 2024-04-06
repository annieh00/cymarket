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
@RequestMapping("/announcements/{userid}")
public class AnnouncementController {

    @Autowired
    private GeneralUserRepository generalUserRepository;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @GetMapping("/")
    @Operation(summary = "List all announcements",
            description = "Lists all announcements for the specified user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Announcements listed successfully")
    })
    public List<Announcement> listAllAnnouncements(@PathVariable int userid) {
        GeneralUser user = generalUserRepository.findGeneralUserById(userid);
        if (user == null) {
            return Collections.emptyList();
        }
        // Return the list of announcements associated with the user
        return user.getAnnouncements();
    }

    @GetMapping("/{announcementId}")
    @Operation(summary = "Get announcement by ID",
            description = "Gets the announcement with the specified ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Announcement retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Announcement not found")
    })
    public Announcement getAnnouncementById(@PathVariable int userid, @PathVariable int announcementId) {
        // Retrieve the user from the repository
        GeneralUser user = generalUserRepository.findGeneralUserById(userid);
        if (user == null) {
            return null;
        }
        Announcement a = announcementRepository.findAnnouncementById(announcementId);
        return a;
    }
}
