package com.project.weatherforecast.service;

import com.project.weatherforecast.bean.Response;
import com.project.weatherforecast.config.ForecastTypeEnum;
import com.project.weatherforecast.exception.BaseException;

import java.util.Map;

public interface WeatherService {

    Object fetchWeatherData(Map<String,String> inputParam, ForecastTypeEnum forecastTypeEnum)
            throws BaseException;

}
