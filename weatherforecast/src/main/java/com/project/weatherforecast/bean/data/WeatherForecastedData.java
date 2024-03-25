package com.project.weatherforecast.bean.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
public class WeatherForecastedData {
    @JsonProperty("dt")
    private String date;
    @JsonProperty("main")
    Temperature temperature;
    @JsonProperty("weather")
    List<Weather> weather;
    @JsonProperty("clouds")
    Clouds clouds;
    @JsonProperty("wind")
    Wind wind;
    @JsonProperty("visibility")
    String visibility;
    @JsonProperty("pop")
    String pop;
    @JsonProperty("rain")
    Rain rain;
//    @JsonProperty("sys")
//    System sys;
    @JsonProperty("dt_txt")
    String dateText;
}
