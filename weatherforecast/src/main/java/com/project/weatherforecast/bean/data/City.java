package com.project.weatherforecast.bean.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class City {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String cityName;

    @JsonProperty("coord")
    private Coordinates coordinates;

    @JsonProperty("country")
    private String country;

    @JsonProperty("population")
    private String population;

    @JsonProperty("timezone")
    private String timezone;

    @JsonProperty("sunrise")
    private Long sunRise;

    @JsonProperty("sunset")
    private Long sunSet;
}
