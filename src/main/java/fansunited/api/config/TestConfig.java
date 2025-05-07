package fansunited.api.config;

import java.io.InputStream;
import java.util.Properties;

public class TestConfig {
    public static final String BASE_URI;
    public static final String BASE_PATH;
    public static final String API_KEY;
    public static final String CLIENT_ID;

    static {
        try (InputStream input = Thread.currentThread()
                .getContextClassLoader().getResourceAsStream("config.properties")) {

            Properties prop = new Properties();
            prop.load(input);

            BASE_URI = prop.getProperty("base.url");
            BASE_PATH = prop.getProperty("base.path");
            API_KEY = prop.getProperty("api.key");
            CLIENT_ID = prop.getProperty("client.id");

            System.out.println("Loaded config in TestConfig: " + BASE_URI);

        } catch (Exception ex) {
            throw new RuntimeException("Failed to load config.properties in TestConfig!", ex);
        }
    }
}
