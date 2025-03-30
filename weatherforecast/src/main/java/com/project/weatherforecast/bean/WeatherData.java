package com.project.weatherforecast.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class WeatherData implements Serializable {
    /** dateText */
    private String dateText;
    /** day */
    private String day;
    /** time */
    private String time;
    /** temperature */
    private Double temperature;
    /** description */
    private String description;
    /** additionalDescription */
    private String additionalDescription;
    /** feelsLike */
    private String feelsLike;
    /** minTemp */
    private Double minTemp;
    /** maxTemp */
    private Double maxTemp;
    /** humidity */
    private String humidity;
    /** windSpeed */
    private Double windSpeed;
    /** weatherDetails */
    private String weatherDetails;
    /** weatherIcon */
    private String weatherIcon;
}
