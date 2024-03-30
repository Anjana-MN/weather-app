package com.project.weatherforecast.service.impl;

import com.project.weatherforecast.bean.Response;
import com.project.weatherforecast.bean.TimeWindowResponse;
import com.project.weatherforecast.bean.Units;
import com.project.weatherforecast.bean.WeatherData;
import com.project.weatherforecast.bean.data.WeatherDataList;
import com.project.weatherforecast.exception.BaseException;
import com.project.weatherforecast.service.WeatherService;
import com.project.weatherforecast.util.WeatherUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private WeatherUtils weatherUtils;

    @Value("${weather.units.api}")
    private String weatherApiInUnits;

    @Override
    public Response fetchWeatherData(Map<String, String> inputParam)
            throws BaseException {
        WeatherDataList weatherResponse = weatherUtils.get(weatherApiInUnits,inputParam);
        return weatherUtils.processWeatherResponse(weatherResponse);
    }

    @Override
    public Object fetchTemperatures(Map<String, String> inputParam)
            throws BaseException {
        inputParam.put("units",Units.valueOf(inputParam.get("units").toUpperCase()).getApiUnits());
        WeatherDataList weatherDataList = weatherUtils.get(weatherApiInUnits,inputParam);
        return weatherUtils.fetchTempList(weatherDataList);
    }

    @Override
    public Object fetchDailyForeCast(Map<String, String> inputParam)
            throws BaseException {
        inputParam.put("units",Units.valueOf(inputParam.get("units").toUpperCase()).getApiUnits());
        WeatherDataList weatherDataList = weatherUtils.get(weatherApiInUnits,inputParam);
        List<TimeWindowResponse> response = new LinkedList<>();
        Map<String, Double> dailyTemperature = new HashMap<>();
        weatherDataList.getWeatherForecastedDataList().forEach((forecastedData)-> {
            List<Double> temps = new ArrayList<>();
            String dateKey = forecastedData.getDateText().substring(0,10);
            if (!dailyTemperature.containsKey(dateKey)) {
                temps = weatherDataList.getWeatherForecastedDataList().stream().filter(w->
                        w.getDateText().contains(dateKey))
                .map(w->w.getTemperature().getTemperature()).collect(Collectors.toList());
            }
            if(!temps.isEmpty()) {
                Double avgTemp = (double) Math.round(
                        temps.stream().mapToDouble(Double::doubleValue).sum() / temps.size());
                dailyTemperature.put(forecastedData.getDateText().substring(0,10), avgTemp);
                TimeWindowResponse timeWindowResponse = new TimeWindowResponse();
                timeWindowResponse.setKey(weatherUtils.fetchDay(forecastedData.getDate()));
                timeWindowResponse.setTemperature(avgTemp);
                timeWindowResponse.setWeatherIcon(forecastedData.getWeather().get(0).getWeatherIcon());
                response.add(timeWindowResponse);
            }
        });
        return response;
    }

    private WeatherDataList get(String url)
            throws BaseException {
        WeatherDataList weatherDataList;
        try{
            weatherDataList = restTemplate.getForObject(url,WeatherDataList.class);
        }catch (HttpClientErrorException e){
            throw new BaseException(UUID.randomUUID(),
                    (HttpStatus) e.getStatusCode(),e.getMessage());
        }
        return weatherDataList;
    }
}
