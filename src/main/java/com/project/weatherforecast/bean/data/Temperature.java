package com.project.weatherforecast.bean.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Temperature {
    /** temperature */
    @JsonProperty("temp")
    private Double temperature;
    /** feelsLike */
    @JsonProperty("feels_like")
    private String feelsLike;
    /** minTemp */
    @JsonProperty("temp_min")
    private Double minTemp;
    /** pressure */
    @JsonProperty("temp_max")
    private Double maxTemp;
    /** predictedRain */
    @JsonProperty("pressure")
    private String pressure;
    /** seaLevel */
    @JsonProperty("sea_level")
    private String seaLevel;
    /** groundLevel */
    @JsonProperty("grnd_level")
    private String groundLevel;
    /** humidity */
    @JsonProperty("humidity")
    private String humidity;
    /** tempKf */
    @JsonProperty("temp_kf")
    private String tempKf;
}
