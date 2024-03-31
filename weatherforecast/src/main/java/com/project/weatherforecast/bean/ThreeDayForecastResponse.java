package com.project.weatherforecast.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@Setter
public class ThreeDayForecastResponse extends RepresentationModel<ThreeDayForecastResponse> {

    private List<WeatherData> weatherData;
    private String country;
    private String cityName;

}
