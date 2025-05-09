package utils.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CompetitionResponseDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("name")
    private String name;

    @JsonProperty("country")
    private CountryDto country;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }

    public CountryDto getCountry() {
        return country;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CountryDto {

        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("country_code")
        private String countryCode;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCountryCode() {
            return countryCode;
        }
    }
}
