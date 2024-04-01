package com.project.weatherforecast.service.impl;

import com.project.weatherforecast.bean.ThreeDayForecastResponse;
import com.project.weatherforecast.bean.Units;
import com.project.weatherforecast.bean.WeatherData;
import com.project.weatherforecast.bean.data.WeatherDataList;
import com.project.weatherforecast.bean.data.WeatherForecastedData;
import com.project.weatherforecast.config.ForecastTypeEnum;
import com.project.weatherforecast.exception.BaseException;
import com.project.weatherforecast.service.DataService;
import com.project.weatherforecast.util.CommonUtils;
import com.project.weatherforecast.util.WeatherUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ThreeDayForecastServiceImpl implements DataService {

    @Autowired
    private WeatherUtils weatherUtils;

    @Autowired
    private CommonUtils commonUtils;

    @Value("${weather.units.api}")
    private String weatherApiInUnits;

    @Override
    public boolean isForecastType(ForecastTypeEnum forecastType) {
        return ForecastTypeEnum.THREE_DAY.equals(forecastType);
    }

    @Override
    @Cacheable(cacheNames = "ThreeDayForecast", cacheManager = "cacheManager")
    public Object fetchData(Map<String, String> inputParam)
            throws BaseException {
        WeatherDataList
                weatherDataList = commonUtils.get(weatherApiInUnits,inputParam);
        ThreeDayForecastResponse response = new ThreeDayForecastResponse();
        List<WeatherData> weatherDataLinkedList = new LinkedList<>();
        Map<Object, List<WeatherForecastedData>> groupedData = weatherDataList.getWeatherForecastedDataList()
                .stream().collect(Collectors.groupingBy(
                        weatherForecastedData -> weatherForecastedData.getDateText().substring(0,10)));
        weatherDataList.getWeatherForecastedDataList().forEach(weatherForecastedData -> {
            List<WeatherForecastedData> weatherForecastedDataList = groupedData.get(
                    weatherForecastedData.getDateText().substring(0,10));
            if(weatherForecastedDataList != null && !weatherForecastedDataList.isEmpty()) {
                double tempSum = weatherForecastedDataList.stream().mapToDouble(
                        w -> w.getTemperature().getTemperature()).sum();
                double avgTemp = tempSum / (weatherForecastedDataList.size());
                OptionalDouble minTemp =
                        weatherForecastedDataList.stream().mapToDouble(w -> w.getTemperature().getMinTemp()).min();
                OptionalDouble maxTemp =
                        weatherForecastedDataList.stream().mapToDouble(w -> w.getTemperature().getMaxTemp()).max();
                WeatherData weatherData = new WeatherData();
                BeanUtils.copyProperties(weatherForecastedData.getWeather().getFirst(),
                        weatherData);
                BeanUtils.copyProperties(weatherForecastedData.getTemperature(),
                        weatherData);
                BeanUtils.copyProperties(weatherForecastedData.getWind(),
                        weatherData);
                BeanUtils.copyProperties(weatherForecastedData, weatherData);
                weatherData.setTemperature(avgTemp);
                weatherData.setMaxTemp(maxTemp.getAsDouble());
                weatherData.setMinTemp(minTemp.getAsDouble());
                weatherData.setDay(weatherUtils.fetchDay(weatherForecastedData.getDate(), weatherDataList.getCity().getTimezone()));
                weatherData.setWeatherIcon((String) weatherUtils.fetchWeatherIcon(weatherForecastedDataList,weatherDataList.getCity().getTimezone()));
                Optional<WeatherForecastedData> rain = weatherForecastedDataList.stream().filter(
                        w -> !ObjectUtils.isEmpty(w.getRain())).findAny();
                OptionalDouble wind =
                        weatherForecastedDataList.stream().mapToDouble(w -> w.getWind().getWindSpeed()).max();
                Units unit = Units.valueOf(inputParam.get("units").toUpperCase());
                if (rain.isPresent()) {
                    weatherData.setDescription("Carry umbrella");
                }
                if (avgTemp > unit.getThresholdTemp()) {
                    weatherData.setDescription("Use sunscreen lotion");
                }
                if (wind.getAsDouble() > unit.getThresholdWindSpeed()) {
                    weatherData.setAdditionalDescription(
                            "It’s too windy, watch out!");
                }
                if (wind.getAsDouble() > unit.getStormIndicator()) {
                    weatherData.setAdditionalDescription(
                            "Don’t step out! A Storm is brewing!");
                }
                weatherDataLinkedList.add(weatherData);
                groupedData.remove(
                        weatherForecastedData.getDateText().substring(0, 10));
            }
        });
        response.setWeatherData(weatherDataLinkedList);
        response.setCountry(weatherDataList.getCity().getCountry());
        response.setCityName(weatherDataList.getCity().getCityName());
        return response;
    }
}
