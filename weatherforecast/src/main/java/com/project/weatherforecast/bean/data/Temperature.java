package com.project.weatherforecast.bean.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Temperature {

    @JsonProperty("temp")
    private Double temperature;

    @JsonProperty("feels_like")
    private String feelsLike;

    @JsonProperty("temp_min")
    private String minTemp;

    @JsonProperty("temp_max")
    private String maxTemp;

    @JsonProperty("pressure")
    private String pressure;

    @JsonProperty("sea_level")
    private String seaLevel;

    @JsonProperty("grnd_level")
    private String groundLevel;

    @JsonProperty("humidity")
    private String humidity;

    @JsonProperty("temp_kf")
    private String tempKf;
}
