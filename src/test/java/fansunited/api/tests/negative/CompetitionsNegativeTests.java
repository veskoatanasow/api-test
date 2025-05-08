package fansunited.api.tests.negative;

import fansunited.api.tests.BaseTest;
import fansunited.api.tests.utils.TestHelpers;
import fansunited.api.utils.ApiClient;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.dto.request.CompetitionRequestDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompetitionsNegativeTests extends BaseTest {

    @Test
    @DisplayName("Missing client_id returns 400 Bad Request")
    void missingClientIdReturns400() {
        Response response = ApiClient.getCompetitionsWithoutClientId();
        TestHelpers.assertStatus(response, HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Missing API key returns 401 Unauthorized")
    void missingApiKeyReturns401() {
        Response response = ApiClient.getCompetitionsWithoutApiKey();
        TestHelpers.assertStatus(response, HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Invalid gender value returns 400 Bad Request")
    void invalidGenderReturns400() {
        CompetitionRequestDto dto = new CompetitionRequestDto();
        dto.setGender("invalidGender");

        Response response = ApiClient.getCompetitions(dto);
        TestHelpers.assertStatus(response, HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Invalid competition ID returns 200 with empty data")
    void invalidCompetitionIdReturnsEmptyList() {
        CompetitionRequestDto dto = new CompetitionRequestDto();
        dto.setIds(List.of("invalid:comp:id"));

        Response response = ApiClient.getCompetitions(dto);
        TestHelpers.assertStatus(response, HttpStatus.SC_OK);

        List<?> data = response.jsonPath().getList("data");
        assertTrue(data == null || data.isEmpty());
    }

    @Test
    @DisplayName("Invalid country ID returns 200 with empty data")
    void invalidCountryIdReturnsEmptyList() {
        CompetitionRequestDto dto = new CompetitionRequestDto();
        dto.setCountryId("nonexistent-country-id");

        Response response = ApiClient.getCompetitions(dto);
        TestHelpers.assertStatus(response, HttpStatus.SC_OK);

        List<?> data = response.jsonPath().getList("data");
        assertTrue(data == null || data.isEmpty());
    }
}
