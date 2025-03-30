package com.project.weatherforecast.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import java.io.Serializable;

@Getter
@Setter
public class Response extends RepresentationModel<Response> implements
        Serializable {
    /** weatherData */
    private WeatherData weatherData;
    /** sunRise */
    private String sunRise;
    /** sunSet */
    private String sunSet;
    /** country */
    private String country;
    /** cityName */
    private String cityName;
    /** coordinates */
    private Coordinates coordinates=new Coordinates();

}
