package com.project.weatherforecast.bean.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class City {

    /** id */
    @JsonProperty("id")
    private String id;
    /** cityName */
    @JsonProperty("name")
    private String cityName;
    /** coordinates */
    @JsonProperty("coord")
    private Coordinates coordinates;
    /** country */
    @JsonProperty("country")
    private String country;
    /** population */
    @JsonProperty("population")
    private String population;
    /** timezone */
    @JsonProperty("timezone")
    private Integer timezone;
    /** sunRise */
    @JsonProperty("sunrise")
    private Long sunRise;
    /** sunSet */
    @JsonProperty("sunset")
    private Long sunSet;
}
