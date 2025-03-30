package com.project.weatherforecast.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ThreeDayForecastResponse
        extends RepresentationModel<ThreeDayForecastResponse>
        implements Serializable {
    /** weatherData */
    private List<WeatherData> weatherData;
    /** country */
    private String country;
    /** cityName */
    private String cityName;

}
