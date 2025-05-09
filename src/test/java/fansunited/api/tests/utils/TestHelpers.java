package fansunited.api.tests.utils;

import io.restassured.response.Response;

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
}
