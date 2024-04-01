package com.project.weatherforecast.service.impl;

import com.project.weatherforecast.bean.TimeWindowResponse;
import com.project.weatherforecast.bean.Units;
import com.project.weatherforecast.bean.data.WeatherDataList;
import com.project.weatherforecast.bean.data.WeatherForecastedData;
import com.project.weatherforecast.config.ForecastTypeEnum;
import com.project.weatherforecast.exception.BaseException;
import com.project.weatherforecast.service.DataService;
import com.project.weatherforecast.util.CommonUtils;
import com.project.weatherforecast.util.WeatherUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DailyForecastServiceImpl implements DataService {

    @Autowired
    private WeatherUtils weatherUtils;

    @Autowired
    private CommonUtils commonUtils;

    @Value("${weather.units.api}")
    private String weatherApiInUnits;

    @Override
    public boolean isForecastType(ForecastTypeEnum forecastType) {
        return ForecastTypeEnum.DAILY.equals(forecastType);
    }

    @Override
    @Cacheable(cacheNames = "DailyForecast", cacheManager = "cacheManager")
    public Object fetchData(Map<String, String> inputParam) throws BaseException {
        inputParam.put("units",Units.valueOf(inputParam.get("units").toUpperCase()).getApiUnits());
        WeatherDataList weatherDataList = commonUtils.get(weatherApiInUnits,inputParam);
        List<TimeWindowResponse> response = new LinkedList<>();
        Map<Object, List<WeatherForecastedData>> groupedData = weatherDataList.getWeatherForecastedDataList()
                .stream().collect(Collectors.groupingBy(
                        weatherForecastedData -> weatherForecastedData.getDateText().substring(0,10)));
        weatherDataList.getWeatherForecastedDataList().forEach((forecastedData)-> {
            String dateKey = forecastedData.getDateText().substring(0,10);
            List<WeatherForecastedData> weatherForecastedDataList = groupedData.get(
                    dateKey);
            if (weatherForecastedDataList != null) {
                double tempSum = weatherForecastedDataList.stream().mapToDouble(
                        w -> w.getTemperature().getTemperature()).sum();
                double avgTemp = tempSum / (weatherForecastedDataList.size());
                TimeWindowResponse timeWindowResponse = new TimeWindowResponse();
                timeWindowResponse.setKey(weatherUtils.fetchDay(forecastedData.getDate(),
                        weatherDataList.getCity().getTimezone()));
                timeWindowResponse.setTemperature(avgTemp);
                timeWindowResponse.setWeatherIcon((String) weatherUtils.fetchWeatherIcon(
                        weatherForecastedDataList,weatherDataList.getCity().getTimezone()));
                response.add(timeWindowResponse);
                groupedData.remove(dateKey);
            }
        });
        return response;
    }
}
