package mainPackage;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.context.ActiveProfiles;


import java.util.List;

import static io.restassured.RestAssured.given;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JunhyungShimSystemTest {
    private String globalEmail;
    private String globalUserName;
    @BeforeEach
    void setUp() {
        globalUserName = (new java.util.Date()).toString();
        globalEmail = globalUserName +"@email.com";
        RestAssured.registerParser("text/plain", Parser.JSON);
        RestAssured.port = 8080;
    }
    @Test
    @Order(1)
    void signupTest(){

        String json = "{\"firstName\": \"ff\",\"lastName\": \"efg\",\"email\": \""+globalEmail+"\",\"password\": \"password\"}";
        Response r =  given().contentType(MediaType.APPLICATION_JSON_VALUE).body(json).when().post("/signup");
        r.then().statusCode(200);
        JsonPath jp = new JsonPath(r.asString());
        String response = jp.get("fromServer").toString();
        Assertions.assertEquals("true", response);
        json = "{\"userName\" : \"" + globalUserName + "\"}";
        Response r2 =  given().contentType(MediaType.APPLICATION_JSON_VALUE).body(json).when().post("/deleteUser");
        r2.then().statusCode(200);
        System.out.println(r.asString());
    }

    @Test
    @Order(4)
    void loginTest(){
        System.out.println(globalEmail);
        String json = "{\"firstName\": \"ff\",\"lastName\": \"efg\",\"email\": \"email@email.com\",\"password\" : \"password\"}";
        Response r =  given().contentType(MediaType.APPLICATION_JSON_VALUE).body(json).when().post("/login");
        System.out.println("TOSTRING: " + r.asString());
        //JsonObject jo = new Gson().fromJson(r.asString(), JsonObject.class);
        r.then().statusCode(200);


        Assertions.assertEquals(r.asString().contains("true"),true);

    }

    //run pipeline
    @Test
    @Order(2)
    void readPosts(){

        Response r = given().get("/getAllPosts");
        r.then().statusCode(200);
        JsonPath jp = new JsonPath(r.asString());
        String list  = jp.get("posts").toString();
        Assertions.assertEquals(false, list.length() == 0);
        System.out.println(list);
    }


    @Test
    @Order(3)
    void makePost(){
        String title = (new java.util.Date()).toString();
        String json = "{\n" +
                "    \"userName\": \"email\",\n" +
                "    \"title\": \""+title+"\""+",\n" +
                "    \"description\": \"description\",\n" +
                "    \"isAuction\": false,\n" +
                "    \"isDonation\": false,\n" +
                "    \"isClosed\": false,\n" +
                "    \"picture1\": \"\",\n" +
                "    \"picture2\": \"\",\n" +
                "    \"picture3\": \"\",\n" +
                "    \"picture4\": \"\",\n" +
                "    \"picture5\": \"\",\n" +
                "    \"picture6\": \"\",\n" +
                "    \"categories\": [\"Arts and Crafts\"],\n" +
                "    \"price\": 110,\n" +
                "    \"id\": 39\n" +
                "  }";

        Response r =  given().contentType(MediaType.APPLICATION_JSON_VALUE).body(json).when().post("/posts");
        r.then().statusCode(200);
        JsonPath jp = new JsonPath(r.asString());
        System.out.println(r.asString());


    }
    

}