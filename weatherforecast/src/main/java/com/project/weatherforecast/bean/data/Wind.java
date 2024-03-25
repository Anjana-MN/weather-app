package com.project.weatherforecast.bean.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Wind {
    @JsonProperty("speed")
    private String windSpeed;

    @JsonProperty("deg")
    private String deg;

    @JsonProperty("gust")
    private String gust;
}
