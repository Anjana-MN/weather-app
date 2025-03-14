package com.project.weatherforecast.util;

import com.project.weatherforecast.bean.TimeWindowResponse;
import com.project.weatherforecast.bean.Units;
import com.project.weatherforecast.bean.WeatherData;
import com.project.weatherforecast.bean.data.*;
import com.project.weatherforecast.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@Slf4j
public class WeatherUtils {

    @Autowired
    private CommonUtils commonUtils;

    @Value("${weather.units.api}")
    private String weatherApiInUnits;

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
        LocalDate localDate = LocalDate.ofInstant(instant, zoneId);
        return localDate.getDayOfWeek().toString();
    }

    public double calAvgTemp(List<WeatherForecastedData> weatherForecastedDataList){
        double tempSum = weatherForecastedDataList.stream().mapToDouble(
                w -> w.getTemperature().getTemperature()).sum();
        return tempSum / (weatherForecastedDataList.size());
    }

    public WeatherData setDateTime(WeatherData weatherData, WeatherForecastedData weatherForecastedData, City city) {
        Instant current = Instant.now();
        weatherData.setDay(fetchDay(weatherForecastedData.getDate(),city.getTimezone()));
        weatherData.setTime(fetchTime(current.getEpochSecond(),city.getTimezone()));
        weatherData.setDateText(fetchDate(weatherForecastedData.getDate(),city.getTimezone()));
        return weatherData;
    }
    public TimeWindowResponse populateTimeWindowResponse(String key, String weatherIcon, double temp){
        TimeWindowResponse timeWindowResponse = new TimeWindowResponse();
        timeWindowResponse.setKey(key);
        timeWindowResponse.setTemperature(temp);
        timeWindowResponse.setWeatherIcon(weatherIcon);
        return timeWindowResponse;
    }

    public WeatherDataList getWeatherDataList(Map<String, String> inputParam) {
        inputParam.put(Constants.UNITS, Units.valueOf(inputParam.get(Constants.UNITS).toUpperCase()).getApiUnits());
        return commonUtils.get(weatherApiInUnits, inputParam);
    }

    public Map<Object, List<WeatherForecastedData>> groupWeatherListByDate(WeatherDataList weatherDataList) {
        return weatherDataList.getWeatherForecastedDataList()
                .stream().collect(Collectors.groupingBy(
                        weatherForecastedData -> weatherForecastedData.getDateText().substring(0, 10)));
    }
}
