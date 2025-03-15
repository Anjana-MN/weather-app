package com.project.weatherforecast.service;

import com.project.weatherforecast.bean.Units;
import com.project.weatherforecast.bean.WeatherData;
import com.project.weatherforecast.bean.data.WeatherDataList;
import com.project.weatherforecast.bean.data.WeatherForecastedData;
import com.project.weatherforecast.constants.Constants;
import com.project.weatherforecast.exception.BaseException;
import com.project.weatherforecast.util.WeatherUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Slf4j
public abstract class AbstractWeatherDataProcessor {

    @Autowired
    private WeatherUtils weatherUtils;

    public Object fetchWeatherCommon(Map<String, String> inputParam, Units unit) throws BaseException {
        log.info("Entering fetchDailyForecast");
        inputParam.put(Constants.COUNT, String.valueOf(weatherUtils.adjustCountBasedOnCurrentTime(inputParam.get(Constants.COUNT))));
        WeatherDataList weatherDataList = weatherUtils.getWeatherDataList(inputParam);
        Map<Object, List<WeatherForecastedData>> groupedData = weatherUtils.groupWeatherListByDate(weatherDataList);
        return processWeatherData(weatherDataList, groupedData,unit);
    }

    public abstract Object processWeatherData(WeatherDataList weatherDataList, Map<Object, List<WeatherForecastedData>> groupedData, Units unit);


    /**
     * populates additional fields
     * @param avgTemp avgTemp
     * @param weatherData weatherData
     * @param rain rain
     * @param wind wind
     * @param unit unit
     */
    protected void populateAdditionalFields(double avgTemp, WeatherData weatherData,
                                          Optional<WeatherForecastedData> rain, OptionalDouble wind,
                                          Units unit) {
        if (rain.isPresent()) {
            weatherData.setDescription("Carry umbrella");
        }
        if (avgTemp > unit.getThresholdTemp()) {
            weatherData.setDescription("Use sunscreen lotion");
        }
        if (wind.isPresent() && wind.getAsDouble() > unit.getThresholdWindSpeed()) {
            weatherData.setAdditionalDescription(
                    "It’s too windy, watch out!");
        }
        if (wind.isPresent() && wind.getAsDouble() > unit.getStormIndicator()) {
            weatherData.setAdditionalDescription(
                    "Don’t step out! A Storm is brewing!");
        }
    }
}
