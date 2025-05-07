package utils.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;

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

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (type != null) map.put("type", type);
        if (gender != null) map.put("gender", gender);
        if (countryId != null) map.put("country_id", countryId);
        if (lang != null) map.put("lang", lang);
        return map;
    }
    @JsonProperty("name")
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
