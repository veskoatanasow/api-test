package fansunited.api.tests.positive;

import fansunited.api.tests.BaseTest;
import fansunited.api.utils.ApiClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import utils.dto.request.CompetitionRequestDto;
import utils.dto.response.CompetitionResponseDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CompetitionsPositiveTests extends BaseTest {

    @Test
    @DisplayName("GET /competitions with only API key and client ID returns list")
    void getAllCompetitions() {
        Response response = ApiClient.getCompetitionsWithOnlyKey();
        assertEquals(200, response.statusCode());

        List<CompetitionResponseDto> data = response.jsonPath().getList("data", CompetitionResponseDto.class);
        assertNotNull(data);
        assertFalse(data.isEmpty(), "Expected at least one competition");
    }

    @Test
    @DisplayName("Filter by gender = female returns only female competitions")
    void filterByGenderFemale() {
        CompetitionRequestDto filters = new CompetitionRequestDto();
        filters.setGender("female");

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(200, response.statusCode());

        List<CompetitionResponseDto> competitions = response.jsonPath().getList("data", CompetitionResponseDto.class);
        assertFalse(competitions.isEmpty());

        for (CompetitionResponseDto comp : competitions) {
            assertEquals("female", comp.getGender(), "Competition gender mismatch");
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"cup", "league", "playoff"})
    @DisplayName("Filter by competition type returns only matching types")
    void filterByType(String type) {
        CompetitionRequestDto filters = new CompetitionRequestDto();
        filters.setType(type);

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(200, response.statusCode());

        List<CompetitionResponseDto> competitions = response.jsonPath().getList("data", CompetitionResponseDto.class);
        assertFalse(competitions.isEmpty());

        for (CompetitionResponseDto comp : competitions) {
            assertEquals(type.toLowerCase(), comp.getType().toLowerCase());
        }
    }

    @Test
    @DisplayName("Filter by gender, type and countryId (AND logic)")
    void filterByMultipleFields() {
        CompetitionRequestDto filters = new CompetitionRequestDto();
        filters.setGender("male");
        filters.setType("league");
        filters.setCountryId("fb:cnt:58");

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(200, response.statusCode());

        List<CompetitionResponseDto> competitions = response.jsonPath().getList("data", CompetitionResponseDto.class);
        assertFalse(competitions.isEmpty());

        for (CompetitionResponseDto comp : competitions) {
            assertEquals("male", comp.getGender());
            assertEquals("league", comp.getType().toLowerCase());
            assertEquals("fb:cnt:58", comp.getCountry().get("id").asText());
        }
    }

    @Test
    @DisplayName("Filter by name returns at least one matching competition")
    void testNameFilterPositive() {
        CompetitionRequestDto filters = new CompetitionRequestDto();
        filters.setName("Primera");

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(200, response.statusCode());

        List<CompetitionResponseDto> competitions = response.jsonPath().getList("data", CompetitionResponseDto.class);
        assertFalse(competitions.isEmpty());

        boolean matchFound = competitions.stream()
                .anyMatch(comp -> comp.getName().toLowerCase().contains("primera"));

        assertTrue(matchFound, "Expected at least one competition containing 'Primera' in the name.");
    }

    @Test
    @DisplayName("Filter by non-matching name returns no results containing the name")
    void testNameFilterNoMatch() {
        CompetitionRequestDto filters = new CompetitionRequestDto();
        filters.setName("NoSuchCompetitionXYZ");

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(200, response.statusCode());

        List<CompetitionResponseDto> competitions = response.jsonPath().getList("data", CompetitionResponseDto.class);

        boolean anyMatch = competitions.stream()
                .anyMatch(c -> c.getName().toLowerCase().contains("nosuchcompetitionxyz"));

        assertFalse(anyMatch, "Expected no competition name to contain 'NoSuchCompetitionXYZ'");
    }
}
