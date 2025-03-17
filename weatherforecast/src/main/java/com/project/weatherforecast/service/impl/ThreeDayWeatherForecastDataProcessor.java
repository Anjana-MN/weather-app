package com.project.weatherforecast.service.impl;

import com.project.weatherforecast.bean.ThreeDayForecastResponse;
import com.project.weatherforecast.bean.Units;
import com.project.weatherforecast.bean.WeatherData;
import com.project.weatherforecast.bean.data.City;
import com.project.weatherforecast.bean.data.WeatherDataList;
import com.project.weatherforecast.bean.data.WeatherForecastedData;
import com.project.weatherforecast.constants.Constants;
import com.project.weatherforecast.exception.BaseException;
import com.project.weatherforecast.service.AbstractWeatherDataProcessor;
import com.project.weatherforecast.service.WeatherForecastDataProcessor;
import com.project.weatherforecast.util.WeatherUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Slf4j
@Service
public class ThreeDayWeatherForecastDataProcessor extends AbstractWeatherDataProcessor implements WeatherForecastDataProcessor {

    @Autowired
    WeatherUtils weatherUtils;

    /**
     * fetches forecast for three days
     * @param inputParam inputParam
     * @return ThreeDayForecastResponse
     * @throws BaseException BaseException
     */
    @Override
    public Object fetchWeather(Map<String, String> inputParam)
            throws BaseException {
        log.info("Entering fetchThreeDayForecast");
        String unit = inputParam.get(Constants.UNITS);
        return fetchWeatherCommon(inputParam, Units.valueOf(unit.toUpperCase()));

    }

    @Override
    public Object processWeatherData(WeatherDataList weatherDataList, Map<Object, List<WeatherForecastedData>> groupedData, Units unit) {
        ThreeDayForecastResponse response = new ThreeDayForecastResponse();
        List<WeatherData> weatherDataLinkedList = new LinkedList<>();
        if(weatherDataList.getCod().equalsIgnoreCase(String.valueOf(HttpStatusCode.valueOf(200)))) {
            try {
                City city = weatherDataList.getCity();
                weatherDataList.getWeatherForecastedDataList().forEach(weatherForecastedData -> {
                    Map<String, Object> weatherMap = weatherUtils.fetchWeatherMap(
                            weatherForecastedData, groupedData, weatherDataList);
                    List<WeatherForecastedData> weatherForecastedDataList = (List<WeatherForecastedData>) weatherMap.get("weatherForecastedDataList");
                    if (!ObjectUtils.isEmpty(weatherForecastedDataList)) {
                        weatherForecastedData = (WeatherForecastedData) weatherMap.get("forecastedData");
                        double avgTemp = (double) weatherMap.get("avgTemp");
                        WeatherData weatherData = weatherUtils.processWeatherResponse(
                                weatherForecastedData, weatherForecastedDataList);
                        weatherData.setTemperature(avgTemp);
                        weatherData = weatherUtils.setDateTime(weatherData, weatherForecastedData, city, weatherForecastedDataList);
                        populateAdditionalFields(avgTemp, weatherData, weatherForecastedDataList, unit);
                        weatherDataLinkedList.add(weatherData);
                    }
                });
                response.setWeatherData(weatherDataLinkedList);
                response.setCountry(weatherDataList.getCity().getCountry());
                response.setCityName(weatherDataList.getCity().getCityName());
            } catch (Exception e) {
                log.error("Exception occurred: {}", e.getMessage());
                throw new BaseException(e.getMessage(), HttpStatusCode.valueOf(500));
            }
        }
        log.info("Exiting fetchThreeDayForecast");
        return response;
    }
}
