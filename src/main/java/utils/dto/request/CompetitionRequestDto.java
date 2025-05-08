package utils.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
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

    @JsonProperty("name")
    private String name;

    @JsonProperty("competition_ids")
    private List<String> ids;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (type != null) map.put("type", type);
        if (gender != null) map.put("gender", gender);
        if (countryId != null) map.put("country_id", countryId);
        if (lang != null) map.put("lang", lang);
        if (name != null) map.put("name", name);
        if (ids != null && !ids.isEmpty()) map.put("competition_ids", String.join(",", ids));
        return map;
    }
}
