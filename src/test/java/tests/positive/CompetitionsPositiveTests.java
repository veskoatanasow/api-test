package tests.positive;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import utils.ApiClient;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class CompetitionsPositiveTests {

    @Test
    @DisplayName("GET /competitions with ONLY API key")
    void onlyApiKeyTest() {
        Response response = ApiClient.getCompetitionsWithOnlyKey();
        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("Filter by gender = female")
    void filterByGender() {
        Map<String, Object> filters = new HashMap<>();
        filters.put("gender", "female");

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(200, response.statusCode());
        assertFalse(response.jsonPath().getList("data").isEmpty());
    }

    @Test
    @DisplayName("Filter by multiple fields (AND logic)")
    void filterByMultipleFields() {
        Map<String, Object> filters = new HashMap<>();
        filters.put("gender", "female");
        filters.put("type", "Cup");
        filters.put("country_id", "fb:cnt:44");

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(200, response.statusCode());
    }

    @ParameterizedTest
    @ValueSource(strings = {"cup", "league", "playoff"})
    @DisplayName("Filter by type")
    void filterByType(String type) {
        Map<String, Object> filters = new HashMap<>();
        filters.put("type", type);

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(200, response.statusCode(), "Failed for type: " + type);
        assertFalse(response.jsonPath().getList("data").isEmpty(), "No competitions found for type: " + type);
    }

    @Test
    @DisplayName("Filter with empty string for gender")
    void filterWithEmptyGender() {
        Map<String, Object> filters = new HashMap<>();
        filters.put("gender", "");

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("Filter with null value for type")
    void filterWithNullType() {
        Map<String, Object> filters = new HashMap<>();
        filters.put("type", null);

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(200, response.statusCode());
    }
}
