package com.project.weatherforecast.util;

import com.project.weatherforecast.bean.Response;
import com.project.weatherforecast.bean.TimeWindowResponse;
import com.project.weatherforecast.bean.WeatherData;
import com.project.weatherforecast.bean.data.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Slf4j
public class WeatherUtils {

    public Response processWeatherResponse(WeatherDataList weatherDataList) {
        Response response = new Response();
        WeatherForecastedData forecastedData = weatherDataList.getWeatherForecastedDataList().get(0);
        City city = weatherDataList.getCity();
        response.setSunRise(fetchTime(city.getSunRise()));
        response.setSunSet(fetchTime(city.getSunSet()));
        BeanUtils.copyProperties(city.getCoordinates(),response.getCoordinates());
            WeatherData data = new WeatherData();
            Temperature temperature = forecastedData.getTemperature();
            Wind wind = forecastedData.getWind();
            Weather weather = forecastedData.getWeather().getFirst();
            Instant instant = Instant.ofEpochSecond(Long.parseLong(forecastedData.getDate()));
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant,
                    ZoneId.systemDefault());
            data.setDay(localDateTime.getDayOfWeek());
            BeanUtils.copyProperties(temperature,data);
            BeanUtils.copyProperties(wind,data);
            BeanUtils.copyProperties(weather,data);
            BeanUtils.copyProperties(forecastedData,data);
            BeanUtils.copyProperties(forecastedData,data);
            BeanUtils.copyProperties(city,data);
            BeanUtils.copyProperties(city,response);
            if(!ObjectUtils.isEmpty(forecastedData.getRain())){
                data.setDescription("Carry umbrella");
            }
            //units=metric for celsius, wind speed will be in meter per sec
            if(temperature.getTemperature()>40.00){
                data.setDescription("Use sunscreen lotion");
            }
            //1 meter per sec = 2.237 miles per hour
            if(2.237*Double.parseDouble(forecastedData.getWind().getWindSpeed())>10.00){
                data.setAdditionalDescription("It’s too windy, watch out!");
            }
            if(2.237*Double.parseDouble(forecastedData.getWind().getGust())>39){
                data.setAdditionalDescription("Don’t step out! A Storm is brewing!");
            }
        response.setWeatherData(data);
        return response;
    }

    private String fetchTime(Long epochSecond) {
        Instant instant = Instant.ofEpochSecond(epochSecond);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant,
                ZoneId.systemDefault());
        String time = localDateTime.getHour()+":"+localDateTime.getMinute();
        return LocalTime.parse(time, DateTimeFormatter.ofPattern("H:m")).
                format(DateTimeFormatter.ofPattern("hh:m a"));

    }

    public Object fetchTempList(WeatherDataList weatherDataList) {
        List<TimeWindowResponse> temperatureList = new LinkedList<>();
        weatherDataList.getWeatherForecastedDataList().forEach(weatherForecastedData -> {
            TimeWindowResponse timeWindowResponse = new TimeWindowResponse();
            timeWindowResponse.setKey(fetchTime(Long.valueOf(weatherForecastedData.getDate())));
            timeWindowResponse.setTemperature(weatherForecastedData.getTemperature().getTemperature());
            timeWindowResponse.setWeatherIcon(weatherForecastedData.getWeather().getFirst().getWeatherIcon());
            temperatureList.add(timeWindowResponse);
        });
        return temperatureList;
    }

    public String fetchDate(String epochSecond) {
        Long epochSec = Long.valueOf(epochSecond);
        Instant instant = Instant.ofEpochSecond(epochSec);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant,
                ZoneId.systemDefault());
        return localDateTime.getYear()+"-"+localDateTime.getMonthValue()+"-"+localDateTime.getDayOfMonth();

    }

    public String fetchDay(String epochSecond) {
        Long epochSec = Long.valueOf(epochSecond);
        Instant instant = Instant.ofEpochSecond(epochSec);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant,
                ZoneId.systemDefault());
        return localDateTime.getDayOfWeek().toString();

    }
}
