package com.project.weatherforecast.bean.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
public class WeatherForecastedData {
    /** date */
    @JsonProperty("dt")
    private String date;
    /** weather */
    @JsonProperty("main")
    Temperature temperature;
    /** cod */
    @JsonProperty("weather")
    List<Weather> weather;
    /** clouds */
    @JsonProperty("clouds")
    Clouds clouds;
    /** wind */
    @JsonProperty("wind")
    Wind wind;
    /** visibility */
    @JsonProperty("visibility")
    String visibility;
    /** pop */
    @JsonProperty("pop")
    String pop;
    /** rain */
    @JsonProperty("rain")
    Rain rain;
    /** dateText */
    @JsonProperty("dt_txt")
    String dateText;
}
