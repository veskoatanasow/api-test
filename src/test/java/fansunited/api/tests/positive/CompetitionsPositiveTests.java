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

public class CompetitionsPositiveTests extends BaseTest {

    @Test
    @DisplayName("GET /competitions with only API key and client ID returns list")
    void getAllCompetitions() {
        Response response = ApiClient.getCompetitionsWithOnlyKey();
        TestHelpers.assertStatus(response, HttpStatus.SC_OK);

        List<CompetitionResponseDto> data = response.jsonPath().getList("data", CompetitionResponseDto.class);
        TestHelpers.assertListNotEmpty(data);
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
        TestHelpers.assertCompetitionFields(competitions, "female", null, null);
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
        TestHelpers.assertCompetitionFields(competitions, null, type, null);
    }

    @Test
    @DisplayName("Filter by gender, type and countryId (AND logic)")
    void filterByMultipleFields() {
        CompetitionRequestDto filters = new CompetitionRequestDto();
        filters.setGender("male");
        filters.setType("league");
        filters.setCountryId("fb:cnt:58");

        Response response = ApiClient.getCompetitions(filters);
        TestHelpers.assertStatus(response, HttpStatus.SC_OK);

        List<CompetitionResponseDto> competitions = response.jsonPath().getList("data", CompetitionResponseDto.class);
        TestHelpers.assertListNotEmpty(competitions);
        TestHelpers.assertCompetitionFields(competitions, "male", "league", "fb:cnt:58");
    }
}
