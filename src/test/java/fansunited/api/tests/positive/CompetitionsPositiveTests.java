package fansunited.api.tests.positive;

import fansunited.api.tests.BaseTest;
import fansunited.api.tests.utils.TestHelpers;
import fansunited.api.utils.ApiClient;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
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
        TestHelpers.assertStatus(response, HttpStatus.SC_OK);

        List<CompetitionResponseDto> data = response.jsonPath().getList("data", CompetitionResponseDto.class);
        TestHelpers.assertListNotEmpty(data);

        for (CompetitionResponseDto dto : data) {
            assertNotNull(dto.getId());
            assertNotNull(dto.getName());
            assertNotNull(dto.getGender());
            assertNotNull(dto.getCountry());
            assertNotNull(dto.getCountry().getId());
            assertNotNull(dto.getCountry().getName());
        }
    }

    @Test
    @DisplayName("Filter by competition ID returns exact match")
    void filterByCompetitionId() {
        CompetitionRequestDto filters = new CompetitionRequestDto();
        filters.setIds(List.of("fb:c:1"));

        Response response = ApiClient.getCompetitions(filters);
        TestHelpers.assertStatus(response, HttpStatus.SC_OK);

        List<CompetitionResponseDto> competitions = response.jsonPath().getList("data", CompetitionResponseDto.class);
        TestHelpers.assertListNotEmpty(competitions);
        assertEquals(1, competitions.size(), "Expected exactly one competition");

        CompetitionResponseDto dto = competitions.get(0);
        assertEquals("fb:c:1", dto.getId());
        assertEquals("First Professional League", dto.getName());
        assertEquals("male", dto.getGender());
        assertEquals("league", dto.getType());
        assertNotNull(dto.getCountry());
        assertEquals("fb:cnt:1224", dto.getCountry().getId());
        assertEquals("Bulgaria", dto.getCountry().getName());
    }

    @Test
    @DisplayName("Filter by gender = female returns only female competitions")
    void filterByGenderFemale() {
        CompetitionRequestDto filters = new CompetitionRequestDto();
        filters.setGender("female");

        Response response = ApiClient.getCompetitions(filters);
        TestHelpers.assertStatus(response, HttpStatus.SC_OK);

        List<CompetitionResponseDto> competitions = response.jsonPath().getList("data", CompetitionResponseDto.class);
        TestHelpers.assertListNotEmpty(competitions);

        for (CompetitionResponseDto dto : competitions) {
            assertEquals("female", dto.getGender(), "Expected only female competitions");
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"cup", "league", "playoff"})
    @DisplayName("Filter by competition type returns only matching types")
    void filterByType(String type) {
        CompetitionRequestDto filters = new CompetitionRequestDto();
        filters.setType(type);

        Response response = ApiClient.getCompetitions(filters);
        TestHelpers.assertStatus(response, HttpStatus.SC_OK);

        List<CompetitionResponseDto> competitions = response.jsonPath().getList("data", CompetitionResponseDto.class);
        TestHelpers.assertListNotEmpty(competitions);

        for (CompetitionResponseDto dto : competitions) {
            assertEquals(type, dto.getType(), "Expected competition type to match filter");
        }
    }

    @Test
    @DisplayName("Filter by gender, type and countryId (AND logic)")
    void filterByMultipleFields() {
        CompetitionRequestDto filters = new CompetitionRequestDto();
        filters.setGender("male");
        filters.setType("league");
        filters.setCountryId("fb:cnt:1224");

        Response response = ApiClient.getCompetitions(filters);
        TestHelpers.assertStatus(response, HttpStatus.SC_OK);

        List<CompetitionResponseDto> competitions = response.jsonPath().getList("data", CompetitionResponseDto.class);
        TestHelpers.assertListNotEmpty(competitions);

        for (CompetitionResponseDto dto : competitions) {
            assertEquals("male", dto.getGender(), "Gender should be male");
            assertEquals("league", dto.getType(), "Type should be league");
            assertNotNull(dto.getCountry(), "Country should not be null");
            assertEquals("fb:cnt:1224", dto.getCountry().getId(), "Country ID should be Bulgaria");
            assertEquals("Bulgaria", dto.getCountry().getName(), "Country name should be Bulgaria");
        }
    }

    @Test
    @DisplayName("Filter by name = 'First Professional' returns at least one matching competition")
    void filterByName() {
        CompetitionRequestDto filters = new CompetitionRequestDto();
        filters.setName("First Professional");

        Response response = ApiClient.getCompetitions(filters);
        TestHelpers.assertStatus(response, HttpStatus.SC_OK);

        List<CompetitionResponseDto> competitions = response.jsonPath().getList("data", CompetitionResponseDto.class);
        TestHelpers.assertListNotEmpty(competitions);

        boolean hasMatch = competitions.stream()
                .anyMatch(dto -> dto.getName().toLowerCase().contains("first professional"));

        assertTrue(hasMatch, "Expected at least one competition name to contain 'First Professional'");
    }
}
