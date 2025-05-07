package fansunited.api.utils;

import fansunited.api.config.TestConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.dto.request.CompetitionRequestDto;

import static io.restassured.RestAssured.given;

public class ApiClient {

    private static RequestSpecification getBaseRequestSpec() {
        return given()
                .baseUri(TestConfig.BASE_URI)
                .basePath(TestConfig.BASE_PATH)
                .contentType("application/json");
    }

    public static Response getCompetitions(CompetitionRequestDto filters) {
        RequestSpecification request = getBaseRequestSpec()
                .queryParam("key", TestConfig.API_KEY)
                .queryParam("client_id", TestConfig.CLIENT_ID);

        if (filters.getType() != null) {
            request.queryParam("type", filters.getType());
        }

        if (filters.getGender() != null) {
            request.queryParam("gender", filters.getGender());
        }

        if (filters.getCountryId() != null) {
            request.queryParam("country_id", filters.getCountryId());
        }

        if (filters.getLang() != null) {
            request.queryParam("lang", filters.getLang());
        }

        return request.when().get();
    }

    public static Response getCompetitionsWithOnlyKey() {
        return getBaseRequestSpec()
                .queryParam("key", TestConfig.API_KEY)
                .queryParam("client_id", TestConfig.CLIENT_ID)
                .when()
                .get();
    }

    public static Response getCompetitionsWithoutApiKey() {
        return given()
                .baseUri(TestConfig.BASE_URI)
                .basePath(TestConfig.BASE_PATH)
                .queryParam("client_id", TestConfig.CLIENT_ID)
                .contentType("application/json")
                .when()
                .get();
    }


    public static Response getCompetitionsWithInvalidMethod() {
        return given()
                .baseUri(TestConfig.BASE_URI)
                .basePath(TestConfig.BASE_PATH)
                .header("x-api-key", TestConfig.API_KEY)
                .header("x-client-id", TestConfig.CLIENT_ID)
                .contentType("application/json")
                .when()
                .get();
    }

    public static Response getCompetitionsWithoutClientId() {
        return given()
                .baseUri(TestConfig.BASE_URI)
                .basePath(TestConfig.BASE_PATH)
                .queryParam("key", TestConfig.API_KEY)
                .contentType("application/json")
                .when()
                .get();
    }
}
