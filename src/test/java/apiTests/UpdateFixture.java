package apiTests;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.*;
import static org.testng.Assert.*;

public class UpdateFixture {

    @BeforeClass
    public void beforeClass() {
        baseURI = ConfigurationReader.get("api_uri");
    }


    @Test()
    public void test1() throws IOException {
        Faker faker = new Faker();
        String teamname = faker.team().name();
        String newFixture = changeAttribute(teamname);

        ValidatableResponse response = given().contentType(ContentType.JSON).and()
                .and().body(newFixture)
                .when().put("/fixture").then().assertThat().statusCode(204);

        Response updatedFixture = given().accept(ContentType.JSON)
                .and().pathParam("id", "1")
                .when().get("/fixture/{id}");

        assertEquals(updatedFixture.statusCode(), 200);

        JsonPath json = updatedFixture.jsonPath();

        String hometeam = json.getString("footballFullState.homeTeam");

        // Assert that the relevant attributes in the fixture have changed.
        assertEquals(hometeam, teamname);


    }


    private String changeAttribute(String team) throws IOException {

        String fileString = new String(Files.readAllBytes(Paths.get("changeFixture.json")));

        return fileString.replace("team_must_be_assigned", String.valueOf(team));

    }


}
