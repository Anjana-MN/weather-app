package com.project.weatherforecast.util;

import com.project.weatherforecast.bean.WeatherData;
import com.project.weatherforecast.bean.data.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Slf4j
public class WeatherUtils {

    /**
     * populates current weather data
     * @param weatherForecastedData weatherForecastedData
     * @return weatherData
     */
    public WeatherData processWeatherResponse(WeatherForecastedData weatherForecastedData) {
            WeatherData weatherData = new WeatherData();
            Temperature temperature = weatherForecastedData.getTemperature();
            Wind wind = weatherForecastedData.getWind();
            Weather weather = weatherForecastedData.getWeather().getFirst();
            BeanUtils.copyProperties(temperature,weatherData);
            BeanUtils.copyProperties(wind,weatherData);
            BeanUtils.copyProperties(weather,weatherData);
            BeanUtils.copyProperties(weatherForecastedData,weatherData);
            BeanUtils.copyProperties(weatherForecastedData,weatherData);
        return weatherData;
    }

    /**
     * fetches time
     * @param epochSecond epochSecond
     * @param offset offset
     * @return time
     */
    public String fetchTime(Long epochSecond, Integer offset) {
        Instant instant = Instant.ofEpochSecond(epochSecond);
        ZoneId zoneId = ZoneId.ofOffset("UTC", ZoneOffset.ofTotalSeconds(offset));
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant,
                zoneId);
        return LocalTime.parse(localDateTime.toString().substring(11,16),
                DateTimeFormatter.ofPattern("H:m"))
                .format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

    /**
     * fetches weather icon
     * @param weatherForecastedDataList weatherForecastedDataList
     * @param offset offset
     * @return weatherIcon
     */
    public Object fetchWeatherIcon(List<WeatherForecastedData> weatherForecastedDataList, Integer offset) {
        Optional<WeatherForecastedData> forecastedData = weatherForecastedDataList.stream().filter(
                weatherForecastedData -> Arrays.asList("11:00 AM", "11:30 AM").contains(fetchTime(
                        Long.valueOf(weatherForecastedData.getDate()),
                        offset))).findFirst();
        if(forecastedData.isPresent()){
            return forecastedData.get().getWeather().get(0).getWeatherIcon();
        }else{
            return weatherForecastedDataList.get(0).getWeather().get(0).getWeatherIcon();
        }
    }

    /**
     * fetches date
     * @param epochSecond epochSecond
     * @param offset offset
     * @return date
     */
    public String fetchDate(String epochSecond, Integer offset) {
        long epochSec = Long.parseLong(epochSecond);
        Instant instant = Instant.ofEpochSecond(epochSec);
        ZoneId zoneId = ZoneId.ofOffset("UTC", ZoneOffset.ofTotalSeconds(offset));
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        return localDateTime.getDayOfMonth() + " " + localDateTime.getMonth() + " " +localDateTime.getYear();
    }

    /**
     * fetches day
     * @param epochSecond epochSecond
     * @param offset offset
     * @return day
     */
    public String fetchDay(String epochSecond, Integer offset) {
        long epochSec = Long.parseLong(epochSecond);
        Instant instant = Instant.ofEpochSecond(epochSec);
        ZoneId zoneId = ZoneId.ofOffset("UTC", ZoneOffset.ofTotalSeconds(offset));
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        return localDateTime.getDayOfWeek().toString();
    }
}
