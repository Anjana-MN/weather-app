package com.project.weatherforecast.service.impl;

import com.project.weatherforecast.bean.TimeWindowResponse;
import com.project.weatherforecast.bean.TimeWindowResponseList;
import com.project.weatherforecast.bean.data.WeatherDataList;
import com.project.weatherforecast.exception.BaseException;
import com.project.weatherforecast.service.WeatherForecastDataProcessor;
import com.project.weatherforecast.util.WeatherUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import java.util.LinkedList;
import java.util.List;
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
        try {
            List<TimeWindowResponse> tempList = new LinkedList<>();
            weatherDataList.getWeatherForecastedDataList().stream().forEach(weatherForecastedData -> {
                String key = weatherUtils.fetchTime(Long.valueOf(weatherForecastedData.getDate()),
                        weatherDataList.getCity().getTimezone());
                tempList.add(weatherUtils.populateTimeWindowResponse(key,
                        weatherForecastedData.getWeather().getFirst().getWeatherIcon(),
                        weatherForecastedData.getTemperature().getTemperature()));
            });
            temperatureList.setTimeWindowResponses(tempList);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BaseException("Internal Error", HttpStatusCode.valueOf(500));
        }
        log.info("Exiting fetchTimelyForecast");
        return temperatureList;
    }
}
