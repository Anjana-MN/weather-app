package com.project.weatherforecast.service;

import java.util.Map;

public interface WeatherService {

    public Object fetchCurrentWeather(Map<String,String> inputParam);
    public Object fetchTimelyForecast(Map<String,String> inputParam);
    public Object fetchDailyForecast(Map<String,String> inputParam);
    public Object fetchThreeDayForecast(Map<String,String> inputParam);
}
