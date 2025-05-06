package tests.positive;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.ApiClient;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
        filters.put("country_id", "fb:cnt:44"); // Внимание: тук полето трябва да съвпада с API документацията

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(200, response.statusCode());
    }
}
