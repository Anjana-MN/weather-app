package com.project.weatherforecast.bean.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Weather {

    @JsonProperty("id")
    private String id;

    @JsonProperty("main")
    private String weatherDetails;

    @JsonProperty("description")
    private String weatherDescription;

    @JsonProperty("icon")
    private String weatherIcon;
}
