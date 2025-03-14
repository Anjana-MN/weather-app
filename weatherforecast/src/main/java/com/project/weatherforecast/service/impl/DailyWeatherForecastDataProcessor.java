package com.project.weatherforecast.service.impl;

import com.project.weatherforecast.bean.TimeWindowResponse;
import com.project.weatherforecast.bean.data.WeatherDataList;
import com.project.weatherforecast.bean.data.WeatherForecastedData;
import com.project.weatherforecast.exception.BaseException;
import com.project.weatherforecast.service.WeatherForecastDataProcessor;
import com.project.weatherforecast.util.WeatherUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DailyWeatherForecastDataProcessor implements WeatherForecastDataProcessor {

    @Autowired
    private WeatherUtils weatherUtils;

    /**
     * fetches daily forecast
     * @param inputParam inputParam
     * @return temperature list
     * @throws BaseException BaseException
     */
    @Cacheable(value = "DailyForecast", cacheManager = "cacheManager")
    @Override
    public Object fetchWeather(Map<String, String> inputParam)
            throws BaseException {
        log.info("Entering fetchDailyForecast");
        WeatherDataList weatherDataList = weatherUtils.getWeatherDataList(inputParam);
        List<TimeWindowResponse> response = new LinkedList<>();
        Map<Object, List<WeatherForecastedData>> groupedData = weatherUtils.groupWeatherListByDate(weatherDataList);
        //iterating through the weather data
        weatherDataList.getWeatherForecastedDataList().forEach(forecastedData-> {
            String dateKey = forecastedData.getDateText().substring(0,10);
            List<WeatherForecastedData> weatherForecastedDataList = groupedData.get(
                    dateKey);
            if (weatherForecastedDataList != null) {
                if(weatherDataList.getCity().getTimezone()<0){
                    forecastedData = weatherForecastedDataList.getLast();
                }
                double avgTemp = weatherUtils.calAvgTemp(weatherForecastedDataList);
                String key = weatherUtils.fetchDay(forecastedData.getDate(),
                        weatherDataList.getCity().getTimezone());
                String weatherIcon = (String) weatherUtils.fetchWeatherIcon(
                        weatherForecastedDataList,weatherDataList.getCity().getTimezone());
                response.add(weatherUtils.populateTimeWindowResponse(key, weatherIcon, avgTemp));
                //removing the data from groupedData
                groupedData.remove(dateKey);
            }
        });
        log.info("Exiting fetchDailyForecast");
        return response;
    }
}
