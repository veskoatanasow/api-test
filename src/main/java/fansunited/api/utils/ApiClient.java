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
        return getBaseRequestSpec()
                .queryParam("key", TestConfig.API_KEY)
                .queryParam("client_id", TestConfig.CLIENT_ID)
                .queryParams(filters.toMap())
                .get();
    }

    public static Response getCompetitionsWithOnlyKey() {
        return getBaseRequestSpec()
                .queryParam("key", TestConfig.API_KEY)
                .queryParam("client_id", TestConfig.CLIENT_ID)
                .get();
    }

    public static Response getCompetitionsWithoutApiKey() {
        return getBaseRequestSpec()
                .queryParam("client_id", TestConfig.CLIENT_ID)
                .get();
    }

    public static Response getCompetitionsWithoutClientId() {
        return getBaseRequestSpec()
                .queryParam("key", TestConfig.API_KEY)
                .get();
    }
}
