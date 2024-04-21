package com.project.weatherforecast.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class Response extends RepresentationModel<Response> implements
        Serializable {
    private WeatherData weatherData;
    private String sunRise;
    private String sunSet;
    private String country;
    private String cityName;
    private Coordinates coordinates=new Coordinates();

}
