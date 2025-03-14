package com.project.weatherforecast.service.impl;

import com.project.weatherforecast.bean.TimeWindowResponseList;
import com.project.weatherforecast.bean.data.WeatherDataList;
import com.project.weatherforecast.exception.BaseException;
import com.project.weatherforecast.service.WeatherForecastDataProcessor;
import com.project.weatherforecast.util.WeatherUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Map;

@Slf4j
@Service
public class TimelyForecastDataProcessor implements WeatherForecastDataProcessor {

    @Autowired
    private WeatherUtils weatherUtils;

    @Cacheable(value = "TimelyForecast", cacheManager = "cacheManager")
    @Override
    public Object fetchWeather(Map<String, String> inputParam)
            throws BaseException {
        log.info("Entering fetchTimelyForecast");
        WeatherDataList weatherDataList = weatherUtils.getWeatherDataList(inputParam);
        TimeWindowResponseList temperatureList = new TimeWindowResponseList();
        LinkedList tempList = new LinkedList();
        weatherDataList.getWeatherForecastedDataList().forEach(weatherForecastedData -> {
            String key = weatherUtils.fetchTime(Long.valueOf(weatherForecastedData.getDate()),
                    weatherDataList.getCity().getTimezone());
            tempList.add(weatherUtils.populateTimeWindowResponse(key,
                    weatherForecastedData.getWeather().getFirst().getWeatherIcon(),
                    weatherForecastedData.getTemperature().getTemperature()));
        });
        temperatureList.setTimeWindowResponses(tempList);
        log.info("Exiting fetchTimelyForecast");
        return temperatureList;
    }
}
