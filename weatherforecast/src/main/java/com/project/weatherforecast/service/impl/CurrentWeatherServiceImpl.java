package com.project.weatherforecast.service.impl;

import com.project.weatherforecast.bean.*;
import com.project.weatherforecast.bean.data.City;
import com.project.weatherforecast.bean.data.WeatherDataList;
import com.project.weatherforecast.bean.data.WeatherForecastedData;
import com.project.weatherforecast.config.ForecastTypeEnum;
import com.project.weatherforecast.exception.BaseException;
import com.project.weatherforecast.service.DataService;
import com.project.weatherforecast.service.WeatherService;
import com.project.weatherforecast.util.CommonUtils;
import com.project.weatherforecast.util.WeatherUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CurrentWeatherServiceImpl implements DataService {

    @Autowired
    private WeatherUtils weatherUtils;

    @Autowired
    private CommonUtils commonUtils;

    @Value("${weather.units.api}")
    private String weatherApiInUnits;

    @Override
    public boolean isForecastType(ForecastTypeEnum forecastType) {
        return ForecastTypeEnum.CURRENT.equals(forecastType);
    }

    @Override
//    @Cacheable(cacheNames = "CurrentWeather", cacheManager = "cacheManager")
    public Response fetchData(Map<String, String> inputParam)
            throws BaseException {
        Response response = new Response();
        WeatherDataList weatherResponse = commonUtils.get(weatherApiInUnits,inputParam);
        City city = weatherResponse.getCity();
        response.setSunRise(weatherUtils.fetchTime(city.getSunRise(),city.getTimezone()));
        response.setSunSet(weatherUtils.fetchTime(city.getSunSet(),city.getTimezone()));
        BeanUtils.copyProperties(city.getCoordinates(),response.getCoordinates());
        BeanUtils.copyProperties(city,response);
        weatherResponse.getWeatherForecastedDataList().forEach(weatherForecastedData -> {
            WeatherData weatherData = weatherUtils.processWeatherResponse(weatherForecastedData);
            weatherData.setDay(weatherUtils.fetchDay(weatherForecastedData.getDate(),city.getTimezone()));
            BeanUtils.copyProperties(city,weatherData);
            response.setWeatherData(weatherData);
                });
        return response;
    }
}
