package com.project.weatherforecast.service.impl;

import com.project.weatherforecast.bean.ThreeDayForecastResponse;
import com.project.weatherforecast.bean.Units;
import com.project.weatherforecast.bean.WeatherData;
import com.project.weatherforecast.bean.data.City;
import com.project.weatherforecast.bean.data.WeatherDataList;
import com.project.weatherforecast.bean.data.WeatherForecastedData;
import com.project.weatherforecast.constants.Constants;
import com.project.weatherforecast.exception.BaseException;
import com.project.weatherforecast.service.WeatherForecastDataProcessor;
import com.project.weatherforecast.util.WeatherUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Slf4j
@Service
public class ThreeDayWeatherForecastDataProcessor implements WeatherForecastDataProcessor {

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
        WeatherDataList weatherDataList = weatherUtils.getWeatherDataList(inputParam);
        ThreeDayForecastResponse response = new ThreeDayForecastResponse();
        List<WeatherData> weatherDataLinkedList = new LinkedList<>();
        Map<Object, List<WeatherForecastedData>> groupedData = weatherUtils.groupWeatherListByDate(weatherDataList);
        weatherDataList.getWeatherForecastedDataList().forEach(weatherForecastedData -> {
            List<WeatherForecastedData> weatherForecastedDataList = groupedData.get(
                    weatherForecastedData.getDateText().substring(0,10));
            if(!ObjectUtils.isEmpty(weatherForecastedDataList)) {
                if(weatherDataList.getCity().getTimezone()<0){
                    weatherForecastedData = weatherForecastedDataList.getLast();
                }
                double avgTemp = weatherUtils.calAvgTemp(weatherForecastedDataList);
                OptionalDouble minTemp =
                        weatherForecastedDataList.stream().mapToDouble(w -> w.getTemperature().getMinTemp()).min();
                OptionalDouble maxTemp =
                        weatherForecastedDataList.stream().mapToDouble(w -> w.getTemperature().getMaxTemp()).max();
                WeatherData weatherData = weatherUtils.processWeatherResponse(weatherForecastedData);
                weatherData.setTemperature(avgTemp);
                weatherData.setMaxTemp(maxTemp.orElse(0.0));
                weatherData.setMinTemp(minTemp.orElse(0.0));
                City city = weatherDataList.getCity();
                weatherData = weatherUtils.setDateTime(weatherData, weatherForecastedData, city);
                weatherData.setWeatherIcon((String) weatherUtils.fetchWeatherIcon(weatherForecastedDataList,weatherDataList.getCity().getTimezone()));
                Optional<WeatherForecastedData> rain = weatherForecastedDataList.stream().filter(
                        w -> !ObjectUtils.isEmpty(w.getRain())).findAny();
                OptionalDouble wind =
                        weatherForecastedDataList.stream().mapToDouble(w -> w.getWind().getWindSpeed()).max();
//                Units unit = Units.valueOf(inputParam.get(Constants.UNITS).toUpperCase());
                populateAdditionalFields(avgTemp, weatherData, rain, wind, Units.valueOf(unit.toUpperCase()));
                weatherDataLinkedList.add(weatherData);
                groupedData.remove(
                        weatherForecastedData.getDateText().substring(0, 10));
            }
        });
        response.setWeatherData(weatherDataLinkedList);
        response.setCountry(weatherDataList.getCity().getCountry());
        response.setCityName(weatherDataList.getCity().getCityName());
        log.info("Exiting fetchThreeDayForecast");
        return response;
    }

    /**
     * populates additional fields
     * @param avgTemp avgTemp
     * @param weatherData weatherData
     * @param rain rain
     * @param wind wind
     * @param unit unit
     */
    private void populateAdditionalFields(double avgTemp, WeatherData weatherData,
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
