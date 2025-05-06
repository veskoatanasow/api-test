package tests.positive;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import utils.ApiClient;
import utils.dto.request.CompetitionRequestDto;
import utils.dto.response.CompetitionResponseDto;

import java.util.List;

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
        CompetitionRequestDto filters = new CompetitionRequestDto();
        filters.setGender("female");

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(200, response.statusCode());

        List<CompetitionResponseDto> competitions =
                response.jsonPath().getList("data", CompetitionResponseDto.class);

        assertFalse(competitions.isEmpty());
    }

    @Test
    @DisplayName("Filter by multiple fields (AND logic)")
    void filterByMultipleFields() {
        CompetitionRequestDto filters = new CompetitionRequestDto();
        filters.setGender("female");
        filters.setType("Cup");
        filters.setCountryId("fb:cnt:44");

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(200, response.statusCode());
    }

    @ParameterizedTest
    @ValueSource(strings = {"EN", "BG", "RO"})
    @DisplayName("Filter by language using supported lang codes")
    void filterByLang(String langCode) {
        CompetitionRequestDto filters = new CompetitionRequestDto();
        filters.setLang(langCode);

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(200, response.statusCode(), "Failed for lang: " + langCode);

        List<CompetitionResponseDto> competitions =
                response.jsonPath().getList("data", CompetitionResponseDto.class);

        assertFalse(competitions.isEmpty(), "No data returned for lang: " + langCode);
    }

    @ParameterizedTest
    @ValueSource(strings = {"cup", "league", "playoff"})
    @DisplayName("Filter by competition type")
    void filterByType(String type) {
        CompetitionRequestDto filters = new CompetitionRequestDto();
        filters.setType(type);

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(200, response.statusCode(), "Failed for type: " + type);

        List<CompetitionResponseDto> competitions =
                response.jsonPath().getList("data", CompetitionResponseDto.class);

        assertFalse(competitions.isEmpty(), "No competitions found for type: " + type);
    }

    @Test
    @DisplayName("Filter with empty string for gender")
    void filterWithEmptyGender() {
        CompetitionRequestDto filters = new CompetitionRequestDto();
        filters.setGender("");

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("Filter with null value for type")
    void filterWithNullType() {
        CompetitionRequestDto filters = new CompetitionRequestDto();
        filters.setType(null);

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("All returned competitions should match given country_id")
    void filterByCountryId() {
        String countryId = "fb:cnt:103";
        CompetitionRequestDto filters = new CompetitionRequestDto();
        filters.setCountryId(countryId);

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(200, response.statusCode());

        List<CompetitionResponseDto> competitions =
                response.jsonPath().getList("data", CompetitionResponseDto.class);

        assertFalse(competitions.isEmpty());

        for (CompetitionResponseDto comp : competitions) {
            assertEquals(countryId, comp.getCountry().get("id").asText());
        }
    }
}
