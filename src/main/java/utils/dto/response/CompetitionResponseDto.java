package utils.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

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
    private JsonNode country;

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

    public JsonNode getCountry() {
        return country;
    }
}
