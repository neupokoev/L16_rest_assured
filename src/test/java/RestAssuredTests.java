import io.restassured.response.Response;
import model.ColorList;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.text.IsEmptyString.emptyString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.Specs.requestSpecCommon;
import static specs.Specs.responseSpecCommon;

public class RestAssuredTests {

    @Test
    void checkIdAndNameWithGroovy() {
        // @formatter:off
        given().
                spec(requestSpecCommon).
        when()
                .get("/users").
        then()
                .spec(responseSpecCommon)
                .body("data.findAll{it.id}.id.flatten()", hasItems(greaterThan(0), lessThan(7)))
                .body("data.findAll{it.first_name}.first_name.flatten()", hasItem("Emma"));
        // @formatter:on
    }

    @Test
    void check404StatusCode() {
        // @formatter:off
        given().
                spec(requestSpecCommon).
        when()
                .get("/users/23").
        then()
                .statusCode(404);
        // @formatter:on
    }

    @Test
    void checkListNameCombo() {
        ColorList colorList =
                // @formatter:off
                given().
                        spec(requestSpecCommon).
                when()
                        .get("/unknown").
                then()
                        .spec(responseSpecCommon)
                        .body("total", is(12))
                        .extract().response().as(ColorList.class);
                // @formatter:on

        assertThat(colorList.getData().size()).
                withFailMessage("Size of array is not equal expected value!").
                isEqualTo(6);

        assertEquals(colorList.getData().get(0).getId(), 1);
        assertEquals(colorList.getData().get(0).getName(), "cerulean");
    }

    @Test
    void checkCodeFromPost() {
        // use org.json JSONObject to define json
        JSONObject jsonObj = new JSONObject()
                .put("name", "morpheus")
                .put("job", "leader");

        Response response =
                // @formatter:off
                given().
                        spec(requestSpecCommon).
                        body(jsonObj.toString()).
                when()
                        .post("/users").
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
                spec(requestSpecCommon).
        when()
                .delete("/users/2").
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
                        spec(requestSpecCommon).
                when()
                        .delete("/users/2").
                then()
                        .statusCode(204)
                        .extract().asString();
                //@formatter:on
        assertThat(responseString).isEmpty();
    }
}