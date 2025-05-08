package fansunited.api.tests.utils;

import io.restassured.response.Response;
import utils.dto.response.CompetitionResponseDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestHelpers {

    public static void assertStatus(Response response, int expectedStatusCode) {
        assertEquals(expectedStatusCode, response.statusCode(),
                "Expected status code " + expectedStatusCode + " but got " + response.statusCode());
    }

    public static void assertListNotEmpty(List<?> list) {
        assertNotNull(list, "Expected list not to be null");
        assertFalse(list.isEmpty(), "Expected list not to be empty");
    }

    public static void assertCompetitionFields(List<CompetitionResponseDto> competitions,
                                               String expectedGender,
                                               String expectedType,
                                               String expectedCountryId) {
        for (CompetitionResponseDto comp : competitions) {
            if (expectedGender != null) {
                assertEquals(expectedGender.toLowerCase(), comp.getGender().toLowerCase(), "Gender mismatch");
            }
            if (expectedType != null) {
                assertEquals(expectedType.toLowerCase(), comp.getType().toLowerCase(), "Type mismatch");
            }
            if (expectedCountryId != null) {
                assertEquals(expectedCountryId, comp.getCountry().get("id").asText(), "Country ID mismatch");
            }
        }
    }
}
