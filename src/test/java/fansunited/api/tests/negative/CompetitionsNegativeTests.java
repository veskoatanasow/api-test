package fansunited.api.tests.negative;

import fansunited.api.tests.BaseTest;
import fansunited.api.utils.ApiClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.dto.request.CompetitionRequestDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    @DisplayName("GET method on POST-only endpoint returns 400 (not 405)")
    void invalidHttpMethodReturns400() {
        Response response = ApiClient.getCompetitionsWithInvalidMethod();

        assertEquals(400, response.statusCode());
    }

    @Test
    @DisplayName("Edge case: empty strings for all filters")
    void edgeCaseEmptyStrings() {
        CompetitionRequestDto filters = new CompetitionRequestDto();
        filters.setName("");
        filters.setGender("");
        filters.setType("");
        filters.setCountryId("");

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("Edge case: name with special characters (possible injection)")
    void edgeCaseNameInjectionLike() {
        CompetitionRequestDto filters = new CompetitionRequestDto();
        filters.setName("'; DROP TABLE competitions;--");

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(200, response.statusCode());

        List<?> data = response.jsonPath().getList("data");
        assertNotNull(data);
    }

    @Test
    @DisplayName("Edge case: extremely long name string")
    void edgeCaseVeryLongName() {
        CompetitionRequestDto filters = new CompetitionRequestDto();
        filters.setName("a".repeat(5000));

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(200, response.statusCode());
    }
}
