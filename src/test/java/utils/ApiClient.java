package utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.dto.request.CompetitionRequestDto;

public class ApiClient {

    private static final String BASE_URL = ConfigReader.get("baseUrl");
    private static final String API_KEY = ConfigReader.get("apiKey");
    private static final String CLIENT_ID = ConfigReader.get("clientId");

    private static RequestSpecification getRequestSpec() {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .basePath("/competitions")
                .queryParam("key", API_KEY)
                .queryParam("client_id", CLIENT_ID);
    }

    public static Response getCompetitions(CompetitionRequestDto filters) {
        RequestSpecification request = getRequestSpec();

        if (filters.getGender() != null) {
            request.queryParam("gender", filters.getGender());
        }
        if (filters.getType() != null) {
            request.queryParam("type", filters.getType());
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
        return getRequestSpec().when().get();
    }

    public static Response getCompetitionsWithoutKey() {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .queryParam("client_id", CLIENT_ID)
                .basePath("/competitions")
                .when()
                .get();
    }

    public static Response getCompetitionsWithoutClientId() {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .queryParam("key", API_KEY)
                .basePath("/competitions")
                .when()
                .get();
    }
}
