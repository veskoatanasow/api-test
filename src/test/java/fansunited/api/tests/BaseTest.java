package fansunited.api.tests;

import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeAll;

import java.io.InputStream;
import java.util.Properties;

public abstract class BaseTest {

    public static String BASE_URI;
    public static String BASE_PATH;
    public static String API_KEY;
    public static String CLIENT_ID;

    @BeforeAll
    public static void setup() {
        loadConfiguration();

        RestAssured.baseURI = BASE_URI;
        RestAssured.basePath = BASE_PATH;

        RestAssured.filters(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter()
        );

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());
    }

    private static void loadConfiguration() {
        try (InputStream input = Thread.currentThread()
                .getContextClassLoader().getResourceAsStream("config.properties")) {

            Properties prop = new Properties();
            prop.load(input);

            BASE_URI = prop.getProperty("base.url");
            BASE_PATH = prop.getProperty("base.path");
            API_KEY = prop.getProperty("api.key");
            CLIENT_ID = prop.getProperty("client.id");

            System.out.println("Loaded base URI: " + BASE_URI);

        } catch (Exception ex) {
            throw new RuntimeException("Failed to load config.properties!", ex);
        }
    }
}
