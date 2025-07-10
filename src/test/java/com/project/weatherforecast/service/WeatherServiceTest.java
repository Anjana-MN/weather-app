package com.project.weatherforecast.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class WeatherServiceTest {

    @InjectMocks
    private WeatherService weatherService;


    @Test
    public void testGetWeatherData(){
        assertNotNull(weatherService.getWeatherData("timely", Map.of("city","Bangalore")));
    }
}
