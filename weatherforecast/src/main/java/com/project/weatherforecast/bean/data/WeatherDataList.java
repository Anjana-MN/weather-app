package com.project.weatherforecast.bean.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WeatherDataList {
    @JsonProperty("cod")
    private String cod;

    @JsonProperty("message")
    private String message;

    @JsonProperty("cnt")
    private String cnt;

    @JsonProperty("list")
    private List<WeatherForecastedData> weatherForecastedDataList;

    @JsonProperty("city")
    private City city;
}
