package com.project.weatherforecast.bean.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WeatherDataList {
    /** cod */
    @JsonProperty("cod")
    private String cod;
    /** message */
    @JsonProperty("message")
    private String message;
    /** cnt */
    @JsonProperty("cnt")
    private String cnt;
    /** weatherForecastedDataList */
    @JsonProperty("list")
    private List<WeatherForecastedData> weatherForecastedDataList;
    /** city */
    @JsonProperty("city")
    private City city;
}
