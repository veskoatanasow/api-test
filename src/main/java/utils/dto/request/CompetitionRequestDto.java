package utils.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompetitionRequestDto {

    @JsonProperty("type")
    private String type;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("country_id")
    private String countryId;

    @JsonProperty("lang")
    private String lang;

    public String getType() {
        return type;
    }

    public String getGender() {
        return gender;
    }

    public String getCountryId() {
        return countryId;
    }

    public String getLang() {
        return lang;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
