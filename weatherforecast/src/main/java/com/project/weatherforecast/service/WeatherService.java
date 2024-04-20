package com.project.weatherforecast.service;

import com.project.weatherforecast.exception.BaseException;

import java.util.Map;

public interface WeatherService {

    public Object fetchCurrentWeather(Map<String,String> inputParam)
            throws BaseException;
    public Object fetchTimelyForecast(Map<String,String> inputParam)
            throws BaseException;
    public Object fetchDailyForecast(Map<String,String> inputParam)
            throws BaseException;
    public Object fetchThreeDayForecast(Map<String,String> inputParam)
            throws BaseException;
}
