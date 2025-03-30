package com.project.weatherforecast.bean.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Weather {
    /** id */
    @JsonProperty("id")
    private String id;
    /** weatherDetails */
    @JsonProperty("main")
    private String weatherDetails;
    /** weatherDescription */
    @JsonProperty("description")
    private String weatherDescription;
    /** weatherIcon */
    @JsonProperty("icon")
    private String weatherIcon;
}
