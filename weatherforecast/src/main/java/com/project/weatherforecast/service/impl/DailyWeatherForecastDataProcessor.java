package com.project.weatherforecast.service.impl;

import com.project.weatherforecast.bean.TimeWindowResponse;
import com.project.weatherforecast.bean.Units;
import com.project.weatherforecast.bean.data.WeatherDataList;
import com.project.weatherforecast.bean.data.WeatherForecastedData;
import com.project.weatherforecast.constants.Constants;
import com.project.weatherforecast.exception.BaseException;
import com.project.weatherforecast.service.AbstractWeatherDataProcessor;
import com.project.weatherforecast.service.WeatherForecastDataProcessor;
import com.project.weatherforecast.util.WeatherUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DailyWeatherForecastDataProcessor extends AbstractWeatherDataProcessor implements WeatherForecastDataProcessor {

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
        String unit = inputParam.get(Constants.UNITS);
        return fetchWeatherCommon(inputParam, Units.valueOf(unit.toUpperCase()));
    }

    @Override
    public Object processWeatherData(WeatherDataList weatherDataList, Map<Object, List<WeatherForecastedData>> groupedData, Units unit) {
        List<TimeWindowResponse> response = new LinkedList<>();
        try {
            weatherDataList.getWeatherForecastedDataList().forEach(forecastedData -> {
                Map<String, Object> weatherMap = weatherUtils.fetchWeatherMap(forecastedData, groupedData, weatherDataList);
                List<WeatherForecastedData> weatherForecastedDataList = (List<WeatherForecastedData>) weatherMap.get("weatherForecastedDataList");
                if (!ObjectUtils.isEmpty(weatherForecastedDataList)) {
                    forecastedData = (WeatherForecastedData) weatherMap.get("forecastedData");
                    String key = weatherUtils.fetchDay(forecastedData.getDate(),
                            weatherDataList.getCity().getTimezone());
                    String weatherIcon = (String) weatherUtils.fetchWeatherIcon(
                            weatherForecastedDataList, weatherDataList.getCity().getTimezone());
                    response.add(weatherUtils.populateTimeWindowResponse(key, weatherIcon, (double) weatherMap.get("avgTemp")));
                }
            });
        } catch (Exception e) {
            log.error("Exception occurred: {}", e.getMessage());
            throw new BaseException(e.getMessage(), HttpStatusCode.valueOf(500));
        }
        return response;
    }
}
