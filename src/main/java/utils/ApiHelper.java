package utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * API layer used to cross-check employee data against a backend API.
 *
 * NOTE: OrangeHRM's public demo instance does not expose an open REST API for
 * PIM/employee records (it requires OAuth2 client credentials that are not available
 * for the public demo). As permitted by the assessment brief ("simulate API with any
 * public test API like ReqRes"), this helper uses the public ReqRes API to demonstrate
 * the API validation layer, request/response handling, and UI-vs-API cross-check pattern.
 * In a real production framework, swap the base URI and endpoints below for the
 * organization's actual employee API.
 */
public class ApiHelper {

    static {
        RestAssured.baseURI = ConfigReader.get("api.base.url");
    }

    /**
     * Simulates creating/validating an employee record via API using the same
     * first/last name captured from the UI, and returns the response for assertions.
     */
    public static Response createEmployeeRecord(String firstName, String lastName, String jobTitle) {
        String requestBody = "{ \"name\": \"" + firstName + " " + lastName + "\", \"job\": \"" + jobTitle + "\" }";

        return given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post("/users")
                .then()
                .extract()
                .response();
    }

    /**
     * Simulates fetching an employee record via API to cross-check against UI data.
     */
    public static Response getEmployeeRecord(String userId) {
        return given()
                .when()
                .get("/users/" + userId)
                .then()
                .extract()
                .response();
    }

    /**
     * Simulates updating an employee's job title/employment status via API.
     */
    public static Response updateEmployeeRecord(String userId, String jobTitle) {
        String requestBody = "{ \"job\": \"" + jobTitle + "\" }";

        return given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .put("/users/" + userId)
                .then()
                .extract()
                .response();
    }

    /**
     * Simulates deleting an employee record via API.
     */
    public static Response deleteEmployeeRecord(String userId) {
        return given()
                .when()
                .delete("/users/" + userId)
                .then()
                .extract()
                .response();
    }
}
