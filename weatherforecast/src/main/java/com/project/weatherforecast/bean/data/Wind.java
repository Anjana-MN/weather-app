package com.project.weatherforecast.bean.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Wind {
    /** windSpeed */
    @JsonProperty("speed")
    private Double windSpeed;
    /** deg */
    @JsonProperty("deg")
    private String deg;
    /** gust */
    @JsonProperty("gust")
    private String gust;
}
