package com.project.weatherforecast.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class WeatherData implements Serializable {
    private String dateText;
    private String day;
    private Double temperature;
    private String description;
    private String additionalDescription;
    private String feelsLike;
    private Double minTemp;
    private Double maxTemp;
    private String humidity;
    private String windSpeed;
    private String weatherDetails;
    private String weatherIcon;
}
