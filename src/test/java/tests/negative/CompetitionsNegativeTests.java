package tests.negative;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.ApiClient;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CompetitionsNegativeTests {

    @Test
    @DisplayName("Missing client ID should return 400")
    void missingClientId() {
        Response response = ApiClient.getCompetitionsWithoutClientId();
        assertEquals(400, response.statusCode());

        String message = response.jsonPath().getString("error.message");
        assertNotNull(message, "Expected error message to be present");
        assertTrue(message.toLowerCase().contains("client_id"), "Expected error message to mention 'client_id'");
    }

    @Test
    @DisplayName("Invalid gender value should return 400")
    void invalidGenderValue() {
        Map<String, Object> filters = new HashMap<>();
        filters.put("gender", "unknown");

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(400, response.statusCode());

        String message = response.jsonPath().getString("error.message");
        assertNotNull(message);
    }


    @Test
    @DisplayName("Invalid type value returns 400")
    void invalidTypeValue() {
        Map<String, Object> filters = new HashMap<>();
        filters.put("type", "invalid_type");

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(400, response.statusCode());

        String message = response.jsonPath().getString("error.message");
        assertNotNull(message);
    }
}
