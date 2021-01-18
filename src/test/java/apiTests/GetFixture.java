package apiTests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;

import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.*;
import static org.testng.Assert.*;

public class GetFixture {


    @BeforeClass
    public void beforeClass() {
        baseURI = ConfigurationReader.get("api_uri");
    }

    @Test
    public void test1() {
        //Retrieve all fixtures.

        Response response = given().accept(ContentType.JSON).when().get("/fixtures");
        assertEquals(response.statusCode(), 200);
        assertTrue(response.contentType().contains("application/json"));

        JsonPath jsonPath = response.jsonPath();
        List<Object> fixtureId = jsonPath.getList("fixtureId");
        List<Object> fixtureStatus = jsonPath.getList("fixtureStatus");

        //   Assert that there are 3 fixtures within the returned object.
        assertEquals(fixtureId.size(), 3);
        assertEquals(fixtureStatus.size(), 3);

        //Assert that each of the 3 fixtures has a fixtureId value.

        for (int i = 0; i < fixtureId.size(); i++) {

            assertTrue(jsonPath.getInt("fixtureId[" + i + "]") > 0);

        }

    }


    @Test
    public void test2() {
        // test each fixtureId has a value
        for (int i = 1; i <= 3; i++) {
            Response response = given().accept(ContentType.JSON).pathParam("id", i).when().get("fixture/{id}");
            JsonPath jsonPath = response.jsonPath();

            assertEquals(response.statusCode(), 200);
            assertEquals(jsonPath.getInt("fixtureId"), i);

        }


    }


    @Test
    public void deneme() {

        Response response = given().accept(ContentType.JSON).when().get("/fixtures");
        response.prettyPrint();
    }
}