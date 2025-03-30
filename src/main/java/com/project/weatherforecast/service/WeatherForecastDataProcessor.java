package com.project.weatherforecast.service;

import com.project.weatherforecast.exception.BaseException;

import java.util.Map;

public interface WeatherForecastDataProcessor {

    /**
     * fetches current weather details
     * @param inputParam inputParam
     * @return Response
     * @throws BaseException BaseException
     */
    public Object fetchWeather(Map<String,String> inputParam)
            throws BaseException;
}
