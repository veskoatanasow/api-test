package fansunited.api.tests.negative;

import fansunited.api.tests.BaseTest;
import fansunited.api.utils.ApiClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.dto.request.CompetitionRequestDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompetitionsNegativeTests extends BaseTest {

    @Test
    @DisplayName("Missing client_id returns 400 with appropriate message")
    void missingClientIdReturns400() {
        Response response = ApiClient.getCompetitionsWithoutClientId();

        assertEquals(400, response.statusCode());
        String message = response.jsonPath().getString("error.message");
        assertTrue(message.toLowerCase().contains("client_id"), "Expected error message for missing client_id");
    }

    @Test
    @DisplayName("Missing API key returns 401 Unauthorized")
    void missingApiKeyReturns401() {
        Response response = ApiClient.getCompetitionsWithoutApiKey();

        assertEquals(401, response.statusCode());
        String message = response.jsonPath().getString("message");
        assertTrue(message != null && !message.isEmpty(), "Expected some error message when API key is missing");
    }

    @Test
    @DisplayName("Invalid gender value returns 400 Bad Request")
    void invalidGenderReturns400() {
        CompetitionRequestDto dto = new CompetitionRequestDto();
        dto.setGender("invalidGender");

        Response response = ApiClient.getCompetitions(dto);

        assertEquals(400, response.statusCode());
        String message = response.jsonPath().getString("error.message");
        assertTrue(message.toLowerCase().contains("invalid gender"), "Expected error message mentioning 'invalid gender'");
    }

    @Test
    @DisplayName("GET method on POST-only endpoint returns 400")
    void invalidHttpMethodReturns400() {
        Response response = ApiClient.getCompetitionsWithInvalidMethod();
        assertEquals(400, response.statusCode());
    }
}
