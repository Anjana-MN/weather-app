package com.project.weatherforecast.bean;

import lombok.Getter;
import lombok.Setter;
import java.time.DayOfWeek;

@Getter
@Setter
public class WeatherData {
    private String dateText;
    private DayOfWeek day;
    private Double temperature;
    private String description;
    private String additionalDescription;
    private String feelsLike;
    private String minTemp;
    private String maxTemp;
    private String humidity;
    private String windSpeed;
    private String weatherDetails;
    private String weatherIcon;
}
