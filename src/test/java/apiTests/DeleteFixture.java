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

public class DeleteFixture {


    @BeforeClass
    public void beforeClass() {
        baseURI = ConfigurationReader.get("api_uri");
    }

    @Test
    public void test1() {

        given().pathParam("id", 1).when().delete("/fixture/{id}").then().statusCode(204);

        //   Assert that the fixture no longer exists.
        given().accept(ContentType.JSON).pathParam("id", "new").when().get("fixture/{id}").then().statusCode(404);

    }


}
