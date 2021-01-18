package apiTests;


import com.github.javafaker.Faker;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.BeforeClass;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.*;
import static org.testng.Assert.*;


public class PostFixture {


    @BeforeClass
    public void beforeClass() {
        baseURI = ConfigurationReader.get("api_uri");
    }

    // @Test(timeOut = 100000)
    @Test()
    public void test1() throws IOException {
        int id = getFixturesSize() + 1;
        String fileString = new String(Files.readAllBytes(Paths.get("createfixture.json")));

        fileString.replace("new", String.valueOf(id));

        String newFixture = new String(Files.readAllBytes(Paths.get("createfixture.json")));
        ValidatableResponse response = given().contentType(ContentType.JSON)
                .and().body(newFixture)
                .when().post("/fixture").then().assertThat().statusCode(202);

        Response newFixtureResponse = given().accept(ContentType.JSON)
                .and().pathParam("id", id)
                .when().get("/fixture/{id}");

        assertEquals(newFixtureResponse.statusCode(), 200);

        JsonPath json = newFixtureResponse.jsonPath();

        //assert within the teams array, that the first object has a teamId of 'HOME'.
        String teamId = json.getString("footballFullState.teams[0].teamId");
        assertEquals(teamId, "HOME");
    }


    private int getFixturesSize() {
        Response response = given().accept(ContentType.JSON)
                .when().get("/fixtures");
        List<Map<String, Object>> allFixtures = response.body().as(List.class);

        return allFixtures.size();
    }


}
