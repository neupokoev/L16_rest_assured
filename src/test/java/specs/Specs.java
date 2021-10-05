package specs;

import com.github.javafaker.Faker;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.json.JSONObject;

import static io.restassured.RestAssured.with;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class Specs {
    final static String BASE_URL = "https://reqres.in";

    public static RequestSpecification requestSpecCommon = with()
            .baseUri(BASE_URL)
            .basePath("/api")
            .log().all()
            .contentType(ContentType.JSON);

    public static ResponseSpecification responseSpecCommon = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .build();

    static Faker faker = new Faker();
    static JSONObject jsonObj = new JSONObject()
            .put("name", faker.name().firstName())
            .put("job", faker.job().title());

    public static RequestSpecification requestToCreate = with()
            .baseUri(BASE_URL)
            .basePath("/api")
            .log().all()
            .contentType(JSON)
            .body(jsonObj.toString());

    static String newFirstName = faker.name().firstName();
    static String newJob = faker.job().title();

    public static ResponseSpecification responseToUpdate = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectBody("name", is(newFirstName))
            .expectBody("job", is(newJob))
            .build();

    static JSONObject jsonObject = new JSONObject()
            .put("name", newFirstName)
            .put("job", newJob);

    public static RequestSpecification requestToUpdate = with()
            .baseUri(BASE_URL)
            .basePath("/api")
            .log().all()
            .contentType(JSON)
            .body(jsonObject.toString());
}
