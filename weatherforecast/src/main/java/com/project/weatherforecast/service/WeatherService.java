package com.project.weatherforecast.service;

import com.project.weatherforecast.bean.Response;
import com.project.weatherforecast.exception.BaseException;

import java.util.List;
import java.util.Map;

public interface WeatherService {

    Object fetchWeatherData(Map<String,String> inputParam);

    Object fetchTemperatures(Map<String, String> inputParam)
            throws BaseException;

    Object fetchDailyForeCast(Map<String, String> inputParam)
            throws BaseException;
}
