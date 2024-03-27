package mainPackage.meetingLocationPackage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Create a new meeting location",
            description = "Creates a new meeting location and saves it to the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Meeting location created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid meeting location data provided")
    })
    public String createMeetingLocation(@RequestBody MeetingLocation m) {
        meetingLocationRepository.save(m);
        String response = "Meeting location created.";
        return "{\"status\": \"" + response + "\"}";
    }

    // Read
    @GetMapping("/meetinglocation/{id}")
    @Operation(summary = "Get a meeting location by ID",
            description = "Returns a meeting location based on the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Meeting location retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Meeting location not found")
    })
    public MeetingLocation readMeetingLocation(@PathVariable(name = "id") int id) {
        return meetingLocationRepository.findMeetingLocationById(id);
    }

    // Update
    @PutMapping("/meetinglocation/update/{id}")
    @Operation(summary = "Update a meeting location",
            description = "Updates an existing meeting location with the provided data in the request body.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Meeting location updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid meeting location data provided"),
            @ApiResponse(responseCode = "404", description = "Meeting location not found")
    })
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
    @Operation(summary = "Delete a meeting location",
            description = "Deletes a meeting location based on the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Meeting location deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Meeting location not found")
    })

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
    @Operation(summary = "Get all meeting locations", description = "Returns a list of all meeting locations.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Meeting locations retrieved successfully")
    })
    public List<MeetingLocation> getAllMeetingLocations() {
        return meetingLocationRepository.findAll();
    }
}
