package mainPackage;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)  // Enables explicit ordering with @Order
class JimmyLeSystemTest {

    @BeforeEach
    void setup() {
        RestAssured.port = 8080;
        RestAssured.registerParser("text/plain", Parser.JSON);
    }

    @Test
    @Order(1)  // First test
    void createAnnouncement() {
        String newAnnouncementJson = "{ \"title\": \"New Announcement\", \"description\": \"This is a new announcement.\" }";

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(newAnnouncementJson)
            .when()
                .post("/announcements/create")
            .then()
                .statusCode(200)
                .body("status", equalTo("Announcement created."))
                .body("id", notNullValue());
    }

    @Test
    @Order(2)  // Second test
    void updateAnnouncement() {
        int announcementIdToUpdate = 28;
        String updateDataJson = "{ \"title\": \"Updated Title\", \"description\": \"Updated description.\" }";

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", announcementIdToUpdate)
                .body(updateDataJson)
            .when()
                .put("/announcements/update/{id}")
            .then()
                .statusCode(200)
                .body("status", equalTo("Announcement updated."));
    }

    @Test
    @Order(3)  // Third test
    void readAnnouncement() {
        int existingAnnouncementId = 28;

        given()
                .pathParam("announcementId", existingAnnouncementId)
            .when()
                .get("/announcements/{announcementId}")
            .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", equalTo(existingAnnouncementId));
    }

    @Test
    @Order(5)  // Fourth test
    void deleteAnnouncement() {
        int announcementIdToDelete = 28;

        given()
                .pathParam("id", announcementIdToDelete)
            .when()
                .delete("/announcements/del/{id}")
            .then()
                .statusCode(200)
                .body("status", equalTo("Announcement deleted successfully."));
    }

    @Test
    @Order(4)  // Last test to confirm at least one announcement
    void getAllAnnouncements() {
        when()
                .get("/announcements")
            .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }
}