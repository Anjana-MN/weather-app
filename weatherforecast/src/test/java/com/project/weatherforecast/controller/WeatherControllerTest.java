package com.project.weatherforecast.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.weatherforecast.bean.Response;
import com.project.weatherforecast.bean.ThreeDayForecastResponse;
import com.project.weatherforecast.bean.TimeWindowResponseList;
import com.project.weatherforecast.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class WeatherControllerTest {

    @InjectMocks
    private WeatherController weatherController;

    @Mock
    private WeatherService weatherService;

    @Test
    public void testGetWeatherForecastForNextThreeDays() throws IOException {
        Map<String,String> inputParam = new HashMap<>();
        inputParam.put("count","10");
        inputParam.put("city","Bangalore");
        inputParam.put("units","celsius");
        ThreeDayForecastResponse threeDayForecastResponse = new ObjectMapper().
                readValue(new File("src/test/resources/json/MockThreeDayResponse.json"),
                ThreeDayForecastResponse.class);
        when(weatherService.fetchThreeDayForecast(inputParam)).thenReturn(threeDayForecastResponse);
        ResponseEntity<Object> response =
                weatherController.getWeatherForecastForNextThreeDays(
                        "10","Bangalore","celsius");
        assertNotNull(response);
    }

    @Test
    public void testGetCurrentWeatherData() throws IOException {
        Map<String,String> inputParam = new HashMap<>();
        inputParam.put("count","10");
        inputParam.put("city","Bangalore");
        inputParam.put("units","celsius");
        Response response = new ObjectMapper().
                readValue(new File("src/test/resources/json/MockResponse.json"),
                        Response.class);
        when(weatherService.fetchCurrentWeather(inputParam)).thenReturn(response);
        ResponseEntity<Object> output =
                weatherController.getCurrentWeatherData(
                        "10","Bangalore","celsius");
        assertNotNull(output);
    }

    @Test
    public void testGetTimelyForecast() throws IOException {
        Map<String,String> inputParam = new HashMap<>();
        inputParam.put("count","10");
        inputParam.put("city","Bangalore");
        inputParam.put("units","celsius");
        TimeWindowResponseList timeWindowResponse = new TimeWindowResponseList();
        when(weatherService.fetchTimelyForecast(inputParam)).thenReturn(timeWindowResponse);
        ResponseEntity<Object> response =
                weatherController.getTimelyForecast(
                        "10","Bangalore","celsius");
        assertNotNull(response);
    }

    @Test
    public void testGetDailyForecast() throws IOException {
        Map<String,String> inputParam = new HashMap<>();
        inputParam.put("count","10");
        inputParam.put("city","Bangalore");
        inputParam.put("units","celsius");
        when(weatherService.fetchDailyForecast(inputParam)).thenReturn(new ArrayList<>());
        ResponseEntity<Object> response =
                weatherController.getDailyForecast(
                        "10","Bangalore","celsius");
        assertNotNull(response);
    }
}
