package com.project.weatherforecast.service.impl;

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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    public Object fetchWeatherData(Map<String, String> inputParam) {
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
        WeatherDataList weatherDataList;
        try{
            weatherDataList = restTemplate.getForObject(url,WeatherDataList.class);
        }catch (Exception e){
            log.info(String.valueOf(e));
            throw new BaseException(UUID.randomUUID(), HttpStatus.BAD_REQUEST,e.getMessage());
        }
        map.put("data",weatherUtils.fetchTempList(weatherDataList));
        return map;
    }

    @Override
    public Object fetchDailyForeCast(Map<String, String> inputParam)
            throws BaseException {
        inputParam.put("units",Units.valueOf(inputParam.get("units").toUpperCase()).getApiUnits());
        String queryParams = commonUtils.buildQuery(inputParam,weatherQueryMap);
        String url = weatherApiInUnits.concat(queryParams);
        WeatherDataList weatherDataList;
        weatherDataList = get(url);
        Map<DayOfWeek, Double> dailyTemperature = new TreeMap<>();
        weatherDataList.getWeatherForecastedDataList().forEach((forecastedData)-> {
            List<Double> temps = new ArrayList<>();
            Instant instant = Instant.ofEpochSecond(Long.parseLong(forecastedData.getDate()));
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant,
                    ZoneId.systemDefault());
            String dateKey = weatherUtils.fetchDate(forecastedData.getDate());
            if (!dailyTemperature.containsKey(localDateTime.getDayOfWeek())) {
                temps = weatherDataList.getWeatherForecastedDataList().stream().filter(w->
                        weatherUtils.fetchDate(w.getDate()).equalsIgnoreCase(dateKey))
                .map(w->w.getTemperature().getTemperature()).collect(Collectors.toList());
            }
            if(!temps.isEmpty()) {
                dailyTemperature.put(localDateTime.getDayOfWeek(), (double) Math.round(
                        temps.stream().mapToDouble(Double::doubleValue).sum() / temps.size()));
            }
        });
        return dailyTemperature;
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
