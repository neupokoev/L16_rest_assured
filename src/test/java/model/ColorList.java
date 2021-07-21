package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ColorList {
    public Integer page;
    public @JsonProperty("per_page")
    Integer perPage;
    public Integer total;
    public Integer total_pages;
    public List<DataUser> data;
}
