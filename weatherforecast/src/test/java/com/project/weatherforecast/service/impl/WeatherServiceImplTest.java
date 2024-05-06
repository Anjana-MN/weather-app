package com.project.weatherforecast.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.weatherforecast.bean.TimeWindowResponseList;
import com.project.weatherforecast.bean.WeatherData;
import com.project.weatherforecast.bean.data.WeatherDataList;
import com.project.weatherforecast.constants.Constants;
import com.project.weatherforecast.util.CommonUtils;
import com.project.weatherforecast.util.WeatherUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.Silent.class)

public class WeatherServiceImplTest {

    @InjectMocks
    private WeatherServiceImpl weatherService;

    @Mock
    private CommonUtils commonUtils;

    @Mock
    private WeatherUtils weatherUtils;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(weatherService, "weatherApiInUnits",
                "/api.openweathermap.org/data/2.5/forecast");
    }

    @Test
    public void testFetchTimelyForecast() throws IOException {
        Map<String,String> inputParam = new HashMap<>();
        inputParam.put(Constants.COUNT,"10");
        inputParam.put(Constants.CITY,"Bangalore");
        inputParam.put(Constants.UNITS,"metric");
        WeatherDataList weatherDataList = new ObjectMapper().readValue(
                new File("src/test/resources/json/MockWeatherDataList.json"),
                WeatherDataList.class);
        TimeWindowResponseList timeWindowResponseList = new ObjectMapper().readValue(
                new File("src/test/resources/json/MockServiceResponse.json"),
                TimeWindowResponseList.class);
        when(commonUtils.get(
                "/api.openweathermap.org/data/2.5/forecast",inputParam))
                .thenReturn(weatherDataList);
        inputParam.put(Constants.UNITS,"celsius");
        assertNotNull(weatherService.fetchTimelyForecast(inputParam));
    }

    @Test
    public void testFetchCurrentWeather() throws IOException {
        Map<String,String> inputParam = new HashMap<>();
        inputParam.put(Constants.COUNT,"10");
        inputParam.put(Constants.CITY,"Bangalore");
        inputParam.put(Constants.UNITS,"metric");
        WeatherDataList weatherDataList = new ObjectMapper().readValue(
                new File("src/test/resources/json/MockWeatherDataList.json"),
                WeatherDataList.class);
        WeatherData weatherData = new ObjectMapper().readValue(
                new File("src/test/resources/json/MockWeatherData.json"),
                WeatherData.class);
        when(commonUtils.get(
                "/api.openweathermap.org/data/2.5/forecast",inputParam))
                .thenReturn(weatherDataList);
        when(weatherUtils.fetchTime(anyLong(),anyInt())).thenReturn("6:30 PM");
        when(weatherUtils.processWeatherResponse(any())).thenReturn(weatherData);
        when(weatherUtils.fetchDay(anyString(),anyInt())).thenReturn("MONDAY");
        inputParam.put(Constants.UNITS,"celsius");
        assertNotNull(weatherService.fetchCurrentWeather(inputParam));
    }

    @Test
    public void testFetchDailyForecast() throws IOException {
        Map<String,String> inputParam = new HashMap<>();
        inputParam.put(Constants.COUNT,"10");
        inputParam.put(Constants.CITY,"Bangalore");
        inputParam.put(Constants.UNITS,"metric");
        WeatherDataList weatherDataList = new ObjectMapper().readValue(
                new File("src/test/resources/json/MockWeatherDataList.json"),
                WeatherDataList.class);
        TimeWindowResponseList timeWindowResponseList = new ObjectMapper().readValue(
                new File("src/test/resources/json/MockServiceResponse.json"),
                TimeWindowResponseList.class);
        when(commonUtils.get(
                "/api.openweathermap.org/data/2.5/forecast",inputParam))
                .thenReturn(weatherDataList);
        when(weatherUtils.fetchDay(anyString(),anyInt())).thenReturn("MONDAY");
        when(weatherUtils.fetchWeatherIcon(any(),anyInt())).thenReturn("04n");
        inputParam.put(Constants.UNITS,"celsius");
        assertNotNull(weatherService.fetchDailyForecast(inputParam));
    }

    @Test
    public void testFetchThreeDayForecast() throws IOException {
        Map<String,String> inputParam = new HashMap<>();
        inputParam.put(Constants.COUNT,"10");
        inputParam.put(Constants.CITY,"Bangalore");
        inputParam.put(Constants.UNITS,"metric");
        WeatherDataList weatherDataList = new ObjectMapper().readValue(
                new File("src/test/resources/json/MockWeatherDataList.json"),
                WeatherDataList.class);
        TimeWindowResponseList timeWindowResponseList = new ObjectMapper().readValue(
                new File("src/test/resources/json/MockServiceResponse.json"),
                TimeWindowResponseList.class);
        when(commonUtils.get(
                "/api.openweathermap.org/data/2.5/forecast",inputParam))
                .thenReturn(weatherDataList);
        when(weatherUtils.fetchDay(anyString(),anyInt())).thenReturn("MONDAY");
        when(weatherUtils.fetchWeatherIcon(any(),anyInt())).thenReturn("04n");
        inputParam.put(Constants.UNITS,"celsius");
        assertNotNull(weatherService.fetchThreeDayForecast(inputParam));
    }
}
