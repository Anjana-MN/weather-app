package com.project.weatherforecast.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Response {
    private WeatherData weatherData;
    private String sunRise;
    private String sunSet;
    private String country;
    private String cityName;
    private Coordinates coordinates=new Coordinates();

}
