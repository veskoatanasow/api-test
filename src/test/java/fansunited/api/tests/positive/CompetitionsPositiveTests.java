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
        assertEquals(STATUS_OK, response.statusCode());

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
        assertEquals(STATUS_OK, response.statusCode());

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
        assertEquals(STATUS_OK, response.statusCode());

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
        assertEquals(STATUS_OK, response.statusCode());

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
        assertEquals(STATUS_OK, response.statusCode());

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
        filters.setName("ProfesionalnaAgrupa");

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(STATUS_OK, response.statusCode());

        List<CompetitionResponseDto> competitions = response.jsonPath().getList("data", CompetitionResponseDto.class);

        boolean anyMatch = competitions.stream()
                .anyMatch(c -> c.getName().toLowerCase().contains("profesionalnaagrupa"));

        assertFalse(anyMatch, "Expected no competition name to contain 'ProfesionalnaAgrupa'");
    }

    @Test
    @DisplayName("Filter by competition IDs returns only specified competitions")
    void filterByCompetitionIds() {
        List<String> expectedIds = List.of("fb:c:9845", "fb:c:99", "fb:c:98", "fb:c:97", "fb:c:1");

        CompetitionRequestDto filters = new CompetitionRequestDto();
        filters.setIds(expectedIds);

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(STATUS_OK, response.statusCode());

        List<CompetitionResponseDto> competitions = response.jsonPath().getList("data", CompetitionResponseDto.class);
        assertFalse(competitions.isEmpty());

        for (CompetitionResponseDto comp : competitions) {
            assertTrue(expectedIds.contains(comp.getId()), "Unexpected competition ID: " + comp.getId());
        }
    }

    @Test
    @DisplayName("Filter by name and gender - validate gender and at least one name match")
    void filterByNameAndGender() {
        CompetitionRequestDto filters = new CompetitionRequestDto();
        filters.setName("liga");
        filters.setGender("male");

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(STATUS_OK, response.statusCode());

        List<CompetitionResponseDto> competitions = response.jsonPath().getList("data", CompetitionResponseDto.class);
        assertFalse(competitions.isEmpty(), "Expected non-empty response for name and gender filter");

        boolean nameMatchFound = false;

        for (CompetitionResponseDto comp : competitions) {
            assertEquals("male", comp.getGender(), "Gender mismatch: expected male");
            if (comp.getName() != null && comp.getName().toLowerCase().contains("liga")) {
                nameMatchFound = true;
            }
        }

        assertTrue(nameMatchFound, "Expected at least one competition to contain 'liga' in its name.");
    }

    @Test
    @DisplayName("Filter by countryId and type returns matching competitions")
    void filterByCountryAndType() {
        CompetitionRequestDto filters = new CompetitionRequestDto();
        filters.setCountryId("fb:cnt:1224");
        filters.setType("cup");

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(STATUS_OK, response.statusCode());

        List<CompetitionResponseDto> competitions = response.jsonPath().getList("data", CompetitionResponseDto.class);
        assertFalse(competitions.isEmpty());

        for (CompetitionResponseDto comp : competitions) {
            assertEquals("cup", comp.getType().toLowerCase());
            assertEquals("fb:cnt:1224", comp.getCountry().get("id").asText());
        }
    }

    @Test
    @DisplayName("Filter by name, gender, and type combined")
    void filterByNameGenderAndType() {
        CompetitionRequestDto filters = new CompetitionRequestDto();
        filters.setName("Super Cup");
        filters.setGender("male");
        filters.setType("cup");

        Response response = ApiClient.getCompetitions(filters);
        assertEquals(STATUS_OK, response.statusCode());

        List<CompetitionResponseDto> competitions = response.jsonPath().getList("data", CompetitionResponseDto.class);
        assertFalse(competitions.isEmpty(), "Expected non-empty response for combined filter");

        boolean nameMatchFound = false;

        for (CompetitionResponseDto comp : competitions) {
            assertEquals("male", comp.getGender(), "Gender mismatch: expected male");
            assertEquals("cup", comp.getType().toLowerCase(), "Type mismatch: expected cup");

            if (comp.getName() != null && comp.getName().toLowerCase().contains("super cup")) {
                nameMatchFound = true;
            }
        }

        assertTrue(nameMatchFound, "Expected at least one competition name to contain 'Super Cup'");
    }
}
