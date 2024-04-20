package com.project.weatherforecast.service.impl;

import com.project.weatherforecast.bean.*;
import com.project.weatherforecast.bean.data.City;
import com.project.weatherforecast.bean.data.WeatherDataList;
import com.project.weatherforecast.bean.data.WeatherForecastedData;
import com.project.weatherforecast.exception.BaseException;
import com.project.weatherforecast.service.WeatherService;
import com.project.weatherforecast.util.CommonUtils;
import com.project.weatherforecast.util.WeatherUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private WeatherUtils weatherUtils;

    @Value("${weather.units.api}")
    private String weatherApiInUnits;

    @Override
    public Object fetchCurrentWeather(Map<String, String> inputParam)
            throws BaseException {
        Response response = new Response();
        WeatherDataList
                weatherResponse = commonUtils.get(weatherApiInUnits,inputParam);
        City city = weatherResponse.getCity();
        response.setSunRise(weatherUtils.fetchTime(city.getSunRise(),city.getTimezone()));
        response.setSunSet(weatherUtils.fetchTime(city.getSunSet(),city.getTimezone()));
        BeanUtils.copyProperties(city.getCoordinates(),response.getCoordinates());
        BeanUtils.copyProperties(city,response);
        weatherResponse.getWeatherForecastedDataList().forEach(weatherForecastedData -> {
            WeatherData weatherData = weatherUtils.processWeatherResponse(weatherForecastedData);
            weatherData.setDay(weatherUtils.fetchDay(weatherForecastedData.getDate(),city.getTimezone()));
            BeanUtils.copyProperties(city,weatherData);
            response.setWeatherData(weatherData);
        });
        return response;
    }

    @Override
    public Object fetchTimelyForecast(Map<String, String> inputParam)
            throws BaseException {
        inputParam.put("units", Units.valueOf(inputParam.get("units").toUpperCase()).getApiUnits());
        WeatherDataList weatherDataList = commonUtils.get(weatherApiInUnits,inputParam);
        TimeWindowResponseList temperatureList = new TimeWindowResponseList();
        LinkedList<TimeWindowResponse> tempList = new LinkedList();
        weatherDataList.getWeatherForecastedDataList().forEach(weatherForecastedData -> {
            TimeWindowResponse timeWindowResponse = new TimeWindowResponse();
            timeWindowResponse.setKey(weatherUtils.fetchTime(Long.valueOf(weatherForecastedData.getDate()),
                    weatherDataList.getCity().getTimezone()));
            timeWindowResponse.setTemperature(weatherForecastedData.getTemperature().getTemperature());
            timeWindowResponse.setWeatherIcon(weatherForecastedData.getWeather().getFirst().getWeatherIcon());
            tempList.add(timeWindowResponse);
        });
        temperatureList.setTimeWindowResponses(tempList);
        return temperatureList;
    }

    @Override
    public Object fetchDailyForecast(Map<String, String> inputParam)
            throws BaseException {
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

    @Override
    public Object fetchThreeDayForecast(Map<String, String> inputParam)
            throws BaseException {
        WeatherDataList
                weatherDataList = commonUtils.get(weatherApiInUnits,inputParam);
        ThreeDayForecastResponse response = new ThreeDayForecastResponse();
        List<WeatherData> weatherDataLinkedList = new LinkedList<>();
        Map<Object, List<WeatherForecastedData>> groupedData = weatherDataList.getWeatherForecastedDataList()
                .stream().collect(Collectors.groupingBy(
                        weatherForecastedData -> weatherForecastedData.getDateText().substring(0,10)));
        weatherDataList.getWeatherForecastedDataList().forEach(weatherForecastedData -> {
            List<WeatherForecastedData> weatherForecastedDataList = groupedData.get(
                    weatherForecastedData.getDateText().substring(0,10));
            if(weatherForecastedDataList != null && !weatherForecastedDataList.isEmpty()) {
                double tempSum = weatherForecastedDataList.stream().mapToDouble(
                        w -> w.getTemperature().getTemperature()).sum();
                double avgTemp = tempSum / (weatherForecastedDataList.size());
                OptionalDouble minTemp =
                        weatherForecastedDataList.stream().mapToDouble(w -> w.getTemperature().getMinTemp()).min();
                OptionalDouble maxTemp =
                        weatherForecastedDataList.stream().mapToDouble(w -> w.getTemperature().getMaxTemp()).max();
                WeatherData weatherData = new WeatherData();
                BeanUtils.copyProperties(weatherForecastedData.getWeather().getFirst(),
                        weatherData);
                BeanUtils.copyProperties(weatherForecastedData.getTemperature(),
                        weatherData);
                BeanUtils.copyProperties(weatherForecastedData.getWind(),
                        weatherData);
                BeanUtils.copyProperties(weatherForecastedData, weatherData);
                weatherData.setTemperature(avgTemp);
                weatherData.setMaxTemp(maxTemp.getAsDouble());
                weatherData.setMinTemp(minTemp.getAsDouble());
                weatherData.setDay(weatherUtils.fetchDay(weatherForecastedData.getDate(), weatherDataList.getCity().getTimezone()));
                weatherData.setWeatherIcon((String) weatherUtils.fetchWeatherIcon(weatherForecastedDataList,weatherDataList.getCity().getTimezone()));
                Optional<WeatherForecastedData> rain = weatherForecastedDataList.stream().filter(
                        w -> !ObjectUtils.isEmpty(w.getRain())).findAny();
                OptionalDouble wind =
                        weatherForecastedDataList.stream().mapToDouble(w -> w.getWind().getWindSpeed()).max();
                Units unit = Units.valueOf(inputParam.get("units").toUpperCase());
                if (rain.isPresent()) {
                    weatherData.setDescription("Carry umbrella");
                }
                if (avgTemp > unit.getThresholdTemp()) {
                    weatherData.setDescription("Use sunscreen lotion");
                }
                if (wind.getAsDouble() > unit.getThresholdWindSpeed()) {
                    weatherData.setAdditionalDescription(
                            "It’s too windy, watch out!");
                }
                if (wind.getAsDouble() > unit.getStormIndicator()) {
                    weatherData.setAdditionalDescription(
                            "Don’t step out! A Storm is brewing!");
                }
                weatherDataLinkedList.add(weatherData);
                groupedData.remove(
                        weatherForecastedData.getDateText().substring(0, 10));
            }
        });
        response.setWeatherData(weatherDataLinkedList);
        response.setCountry(weatherDataList.getCity().getCountry());
        response.setCityName(weatherDataList.getCity().getCityName());
        return response;
    }
}
