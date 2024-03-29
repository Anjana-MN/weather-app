package com.project.weatherforecast.service.impl;

import com.project.weatherforecast.bean.Response;
import com.project.weatherforecast.bean.TimeWindowResponse;
import com.project.weatherforecast.bean.Units;
import com.project.weatherforecast.bean.data.WeatherDataList;
import com.project.weatherforecast.exception.BaseException;
import com.project.weatherforecast.service.WeatherService;
import com.project.weatherforecast.util.CommonUtils;
import com.project.weatherforecast.util.WeatherUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private WeatherUtils weatherUtils;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CommonUtils commonUtils;

    @Value("#{${weather.query.map}}")
    private Map<String,String> weatherQueryMap;

    @Value("${weather.api}")
    private String weatherApi;

    @Value("${weather.units.api}")
    private String weatherApiInUnits;

    @Override
    public Response fetchWeatherData(Map<String, String> inputParam) {
        String queryParams = commonUtils.buildQuery(inputParam,weatherQueryMap);
        String url = weatherApi.concat(queryParams);
        WeatherDataList weatherResponse = restTemplate.getForObject(url,WeatherDataList.class);
        return weatherUtils.processWeatherResponse(weatherResponse);
    }

    @Override
    public Object fetchTemperatures(Map<String, String> inputParam)
            throws BaseException {
        Map<String,Object> map = new HashMap<>();
        inputParam.put("units",Units.valueOf(inputParam.get("units").toUpperCase()).getApiUnits());
        String queryParams = commonUtils.buildQuery(inputParam,weatherQueryMap);
        String url = weatherApiInUnits.concat(queryParams);
        WeatherDataList weatherDataList = get(url);
        map.put("data",weatherUtils.fetchTempList(weatherDataList));
        return map;
    }

    @Override
    public Object fetchDailyForeCast(Map<String, String> inputParam)
            throws BaseException {
        inputParam.put("units",Units.valueOf(inputParam.get("units").toUpperCase()).getApiUnits());
        String queryParams = commonUtils.buildQuery(inputParam,weatherQueryMap);
        String url = weatherApiInUnits.concat(queryParams);
        WeatherDataList weatherDataList = get(url);
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
