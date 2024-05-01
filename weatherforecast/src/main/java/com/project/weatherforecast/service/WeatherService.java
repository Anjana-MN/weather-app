package com.project.weatherforecast.service;

import com.project.weatherforecast.exception.BaseException;

import java.util.Map;

public interface WeatherService {

    /**
     * fetches current weather details
     * @param inputParam inputParam
     * @return Response
     * @throws BaseException BaseException
     */
    public Object fetchCurrentWeather(Map<String,String> inputParam)
            throws BaseException;

    /**
     * fetches timely forecast
     * @param inputParam inputParam
     * @return temperature list
     * @throws BaseException BaseException
     */
    public Object fetchTimelyForecast(Map<String,String> inputParam)
            throws BaseException;

    /**
     * fetches daily forecast
     * @param inputParam inputParam
     * @return temperature list
     * @throws BaseException BaseException
     */
    public Object fetchDailyForecast(Map<String,String> inputParam)
            throws BaseException;

    /**
     * fetches forecast for next three days
     * @param inputParam inputParam
     * @return ThreeDayForecastResponse
     * @throws BaseException BaseException
     */
    public Object fetchThreeDayForecast(Map<String,String> inputParam)
            throws BaseException;
}
