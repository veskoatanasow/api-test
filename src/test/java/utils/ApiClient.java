package utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

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

    public static Response getCompetitions(Map<String, Object> queryParams) {
        RequestSpecification request = getRequestSpec();

        if (queryParams != null) {
            for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
                request.queryParam(entry.getKey(), entry.getValue());
            }
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
