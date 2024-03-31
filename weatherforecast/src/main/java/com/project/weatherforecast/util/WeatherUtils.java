package com.project.weatherforecast.util;

import com.project.weatherforecast.bean.Response;
import com.project.weatherforecast.bean.TimeWindowResponse;
import com.project.weatherforecast.bean.WeatherData;
import com.project.weatherforecast.bean.data.*;
import com.project.weatherforecast.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Slf4j
public class WeatherUtils {

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private RestTemplate restTemplate;

    @Value("#{${weather.query.map}}")
    private Map<String,String> weatherQueryMap;

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

    public String fetchTime(Long epochSecond, Integer offset) {
        Instant instant = Instant.ofEpochSecond(epochSecond);
        ZoneId zoneId = ZoneOffset.ofOffset("UTC", ZoneOffset.ofTotalSeconds(offset));
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant,
                zoneId);
        String time = localDateTime.getHour()+":"+localDateTime.getMinute();
        return LocalTime.parse(time, DateTimeFormatter.ofPattern("hh:m")).
                format(DateTimeFormatter.ofPattern("hh:mm a"));

    }

    public Object fetchTempList(WeatherDataList weatherDataList) {
        List<TimeWindowResponse> temperatureList = new LinkedList<>();
        weatherDataList.getWeatherForecastedDataList().forEach(weatherForecastedData -> {
            TimeWindowResponse timeWindowResponse = new TimeWindowResponse();
            timeWindowResponse.setKey(fetchTime(Long.valueOf(weatherForecastedData.getDate()),
                    weatherDataList.getCity().getTimezone()));
            timeWindowResponse.setTemperature(weatherForecastedData.getTemperature().getTemperature());
            timeWindowResponse.setWeatherIcon(weatherForecastedData.getWeather().getFirst().getWeatherIcon());
            temperatureList.add(timeWindowResponse);
        });
        return temperatureList;
    }

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

    public String fetchDate(String epochSecond, Integer offset) {
        Long epochSec = Long.valueOf(epochSecond);
        Instant instant = Instant.ofEpochSecond(epochSec);
        ZoneId zoneId = ZoneOffset.ofOffset("UTC", ZoneOffset.ofTotalSeconds(offset));
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        return localDateTime.getYear()+"-"+localDateTime.getMonthValue()+"-"+localDateTime.getDayOfMonth();

    }

    public String fetchDay(String epochSecond, Integer offset) {
        Long epochSec = Long.valueOf(epochSecond);
        Instant instant = Instant.ofEpochSecond(epochSec);
        ZoneId zoneId = ZoneOffset.ofOffset("UTC", ZoneOffset.ofTotalSeconds(offset));
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        return localDateTime.getDayOfWeek().toString();

    }

    public WeatherDataList get(String url, Map<String,String> inputParam)
            throws BaseException {
        WeatherDataList weatherDataList;
        String queryParams = commonUtils.buildQuery(inputParam,weatherQueryMap);
        url = url.concat(queryParams);
        try{
            weatherDataList = restTemplate.getForObject(url,WeatherDataList.class);
        }catch (HttpClientErrorException e){
            log.info("Exception: {}{}{}",UUID.randomUUID(), e.getStatusCode(),
                    e.getMessage());
            throw new BaseException(UUID.randomUUID(), e.getStatusCode(),
                    e.getMessage());
        }
        return weatherDataList;
    }
}
