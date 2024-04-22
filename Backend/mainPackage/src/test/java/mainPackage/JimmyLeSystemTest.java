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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JimmyLeSystemTest {

    private static int announcementId;

    @BeforeEach
    void setup() {
        RestAssured.port = 8080;
        RestAssured.registerParser("text/plain", Parser.JSON);
    }

    @Test
    @Order(1)
    void createAnnouncement() {
        String newAnnouncementJson = "{ \"title\": \"New Announcement\", \"description\": \"This is a new announcement.\" }";

        // Ensure the extracted id is treated as an integer
        announcementId =
                Integer.parseInt(given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(newAnnouncementJson)
                        .when()
                        .post("/announcements/create")
                        .then()
                        .statusCode(200)
                        .body("status", equalTo("Announcement created."))
                        .extract()
                        .path("id").toString()); // Ensure id is a string to check
    }

    @Test
    @Order(2)
    void updateAnnouncement() {
        String updateDataJson = "{ \"title\": \"Updated Title\", \"description\": \"Updated description.\" }";

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", announcementId)
                .body(updateDataJson)
            .when()
                .put("/announcements/update/{id}")
            .then()
                .statusCode(200)
                .body("status", equalTo("Announcement updated."));
    }

    @Test
    @Order(3)
    void readAnnouncement() {
        given()
                    .pathParam("announcementId", announcementId)
                .when()
                    .get("/announcements/{announcementId}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body("id", equalTo(announcementId));
    }

    @Test
    @Order(5)
    void deleteAnnouncement() {
        given()
                    .pathParam("id", announcementId)
                .when()
                    .delete("/announcements/del/{id}")
                .then()
                    .statusCode(200)
                    .body("status", equalTo("Announcement deleted successfully."));
    }

    @Test
    @Order(4)
    void getAllAnnouncements() {
        when()
                .get("/announcements")
            .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }
}
