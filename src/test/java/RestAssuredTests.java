import io.restassured.response.Response;
import model.ColorList;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.text.IsEmptyString.emptyString;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RestAssuredTests {

    final static String BASE_URL = "https://reqres.in";

    @Test
    void checkIdAndName() {
        // @formatter:off
        given().
                baseUri(BASE_URL).
                log().uri().
        when()
                .get("/api/users/3").
        then()
                .statusCode(200)
                .body("data.id", is(3))
                .body("data.first_name", is("Emma"));
        // @formatter:on
    }

    @Test
    void check404StatusCode() {
        // @formatter:off
        given().
                baseUri(BASE_URL).
                log().uri().
        when()
                .get("/api/users/23").
        then()
                .statusCode(404);
        // @formatter:on
    }

    @Test
    void checkListName() {
        ColorList colorList =
                // @formatter:off
                given().
                        baseUri(BASE_URL).
                        log().uri().
                when()
                        .get("/api/unknown").
                then()
                        .statusCode(200)
                        .body("total", is(12))
                        .extract().response().as(ColorList.class);
        // @formatter:on

        assertThat(colorList.data.size()).
                withFailMessage("Size of array is not equal expected value!").
                isEqualTo(6);

        assertEquals(colorList.data.get(0).id, 1);
        assertEquals(colorList.data.get(0).name, "cerulean");
    }

    @Test
    void checkCodeFromPost() {
        Response response =
                // @formatter:off
                given().
                        baseUri(BASE_URL)
                        .contentType(JSON)
                        .body("{" +
                                "    \"name\": \"morpheus\"," +
                                "    \"job\": \"leader\"" +
                                "}").
                when()
                        .post("/api/users").
                then()
                        .statusCode(201)
                        .body("name", is("morpheus"))
                        .extract().response();
        // @formatter:on

        assertEquals(response.body().path("job"), "leader");
    }

    @Test
    void checkStatusCodeAfterDelete() {
        // @formatter:off
        given().
                baseUri(BASE_URL).
                log().uri().
        when()
                .delete("/api/users/2").
        then()
                .statusCode(204)
                .body(is(emptyString()));
        // @formatter:on
    }

    @Test
    void checkStatusCodeAfterDeleteOtherWay() {
        String responseString =
                // @formatter:off
                given().
                        baseUri(BASE_URL).
                        log().uri().
                when()
                        .delete("/api/users/2").
                then()
                        .statusCode(204)
                        .extract().asString();
        //@formatter:on
        assertThat(responseString).isEmpty();
    }
}