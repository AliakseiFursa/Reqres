import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ReqresTest {

    @Test
    public void listUsers() {
        given().
                log().all().
        when().
                get("https://reqres.in/api/users?page=2").
        then().
                log().all().
                statusCode(200).
                body("page", equalTo(2), "total", equalTo(12), "data[0].id", equalTo(7),
                        "data[0].first_name", equalTo("Michael"), "data[3].id", equalTo(10), "data[3].first_name",
                        equalTo("Byron"));
    }

    @Test
    public void singleUser() {
        given().
                log().all().
        when().
                get("https://reqres.in/api/users/2").
        then().
                log().all().
                statusCode(200).
                body("data.id", equalTo(2), "data.email", equalTo("janet.weaver@reqres.in"),
                        "data.first_name", equalTo("Janet"), "data.last_name", equalTo("Weaver"));
    }

    @Test
    public void singleUserNotFound() {
        given().
                log().all().
        when().
                get("https://reqres.in/api/users/23").
        then().
                log().all().
                statusCode(404);
    }

    @Test
    public void listResource() {
        given().
                log().all().
        when().
                get("https://reqres.in/api/unknown").
        then().
                log().all().
                statusCode(200).
                body("page", equalTo(1), "per_page", equalTo(6), "data[2].id", equalTo(3),
                        "data[2].name", equalTo("true red"), "data[2].pantone_value", equalTo("19-1664"),
                        "data[4].name", equalTo("tigerlily"), "data[4].color", equalTo("#E2583E"),
                        "support.text", equalTo("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    @Test
    public void singleResource() {
        given().
                log().all().
        when().
                get("https://reqres.in/api/unknown/2").
        then().
                log().all().
                statusCode(200).
                body("data.id", equalTo(2), "data.name", equalTo("fuchsia rose"),
                        "data.pantone_value", equalTo("17-2031"), "support.text",
                        equalTo("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    @Test
    public void singleResourceNotFound() {
        given().
                log().all().
        when().
                get("https://reqres.in/api/unknown/23").
        then().
                log().all().
                statusCode(404);
    }

    @Test
    public void create() {
        int id =
                given().
                body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"leader\"\n" +
                        "}").
                        log().all().
                when().
                        post("https://reqres.in/api/users").
                then().
                        log().all().
                        statusCode(201).
                        extract().body().jsonPath().getInt("id");

        System.out.println("ID equals: " + id);
    }

    @Test
    public void updatePut() {
        String response =
                        given().
                                body("{\n" +
                                        "    \"name\": \"morpheus\",\n" +
                                        "    \"job\": \"zion resident\"\n" +
                                        "}").
                                log().all().
                        when().
                                put("https://reqres.in/api/users/2").
                        then().
                                log().all().
                                statusCode(200).
                                extract().body().asString();

        System.out.println(response);
    }

    @Test
    public void updatePatch() {
        String response =
                        given().
                                body("{\n" +
                                        "    \"name\": \"morpheus\",\n" +
                                        "    \"job\": \"zion resident\"\n" +
                                        "}").
                                log().all().
                        when().
                                patch("https://reqres.in/api/users/2").
                        then().
                                log().all().
                                statusCode(200).
                                extract().body().asString();

        System.out.println(response);
    }

    @Test
    public void delete() {
        given().
                log().all().
        when().
                delete("https://reqres.in/api/users/2").
        then().
                log().all().
                statusCode(204);
    }

    @Test
    public void registerSuccessful() {
        given().
                body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"pistol\"\n" +
                        "}").
                header("Content-Type", "application/json").
                log().all().
        when().
                post("https://reqres.in/api/register").
        then().
                log().all().
                statusCode(200).
                body("id", equalTo(4), "token", equalTo("QpwL5tke4Pnpja7X4"));
    }

    @Test
    public void registerUnsuccessful() {
        given().
                body("{\n" +
                        "    \"email\": \"sydney@fife\"\n" +
                        "}").
                header("Content-Type", "application/json").
                log().all().
        when().
                post("https://reqres.in/api/register").
        then().
                log().all().
                statusCode(400).
                body("error", equalTo("Missing password"));
    }

    @Test
    public void loginSuccessful() {
        given().
                body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"cityslicka\"\n" +
                        "}").
                header("Content-Type", "application/json").
                log().all().
        when().
                post("https://reqres.in/api/login").
        then().
                log().all().
                statusCode(200).
                body("token", equalTo("QpwL5tke4Pnpja7X4"));
    }

    @Test
    public void delayedResponse() {
        given().
                log().all().
                when().
                get("https://reqres.in/api/users?delay=3").
                then().
                log().all().
                statusCode(200).
                body("page", equalTo(1), "total", equalTo(12), "data[0].id", equalTo(1),
                        "data[0].first_name", equalTo("George"), "data[4].id", equalTo(5), "data[4].first_name",
                        equalTo("Charles"));
    }

    @Test
    public void loginUnsuccessful() {
        given().
                body("{\n" +
                        "    \"email\": \"peter@klaven\"\n" +
                        "}").
                header("Content-Type", "application/json").
                log().all().
        when().
                post("https://reqres.in/api/login").
        then().
                log().all().
                statusCode(400).
                body("error", equalTo("Missing password"));
    }


}
