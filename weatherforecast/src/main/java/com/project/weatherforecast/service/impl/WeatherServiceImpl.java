package com.project.weatherforecast.service.impl;

import com.project.weatherforecast.bean.TimeWindowResponse;
import com.project.weatherforecast.bean.TimeWindowResponseList;
import com.project.weatherforecast.bean.Response;
import com.project.weatherforecast.bean.Units;
import com.project.weatherforecast.bean.WeatherData;
import com.project.weatherforecast.bean.ThreeDayForecastResponse;
import com.project.weatherforecast.bean.data.City;
import com.project.weatherforecast.bean.data.WeatherDataList;
import com.project.weatherforecast.bean.data.WeatherForecastedData;
import com.project.weatherforecast.constants.Constants;
import com.project.weatherforecast.exception.BaseException;
import com.project.weatherforecast.service.WeatherService;
import com.project.weatherforecast.util.CommonUtils;
import com.project.weatherforecast.util.WeatherUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {
    /** The commonUtils */
    @Autowired
    private CommonUtils commonUtils;
    /** The weatherUtils */
    @Autowired
    private WeatherUtils weatherUtils;
    /** The weatherApiInUnits */
    @Value("${weather.units.api}")
    private String weatherApiInUnits;

    /**
     * fetches the current weather
     * @param inputParam inputParam
     * @return response
     * @throws BaseException BaseException
     */
    @Override
    public Object fetchCurrentWeather(Map<String, String> inputParam)
            throws BaseException {
        log.info("Entering fetchCurrentWeather");
        Response response = new Response();
        inputParam.put(Constants.UNITS, Units.valueOf(inputParam.get(Constants.UNITS).toUpperCase()).getApiUnits());
        WeatherDataList
                weatherResponse = commonUtils.get(weatherApiInUnits,inputParam);
        City city = weatherResponse.getCity();
        response.setSunRise(weatherUtils.fetchTime(city.getSunRise(),city.getTimezone()));
        response.setSunSet(weatherUtils.fetchTime(city.getSunSet(),city.getTimezone()));
        BeanUtils.copyProperties(city.getCoordinates(),response.getCoordinates());
        BeanUtils.copyProperties(city,response);
        WeatherForecastedData weatherForecastedData = weatherResponse.getWeatherForecastedDataList().get(0);
        WeatherData weatherData = weatherUtils.processWeatherResponse(weatherForecastedData);
        Instant current = Instant.now();
        weatherData.setDay(weatherUtils.fetchDay(weatherForecastedData.getDate(),city.getTimezone()));
        weatherData.setTime(weatherUtils.fetchTime(current.getEpochSecond(),city.getTimezone()));
        weatherData.setDateText(weatherUtils.fetchDate(weatherForecastedData.getDate(),city.getTimezone()));
        BeanUtils.copyProperties(city,weatherData);
        response.setWeatherData(weatherData);
        log.info("Exiting fetchCurrentWeather");
        return response;
    }

    /**
     * fetches timely forecast
     * @param inputParam inputParam
     * @return temperature list
     * @throws BaseException BaseException
     */
    @Cacheable(value = "TimelyForecast", cacheManager = "cacheManager")
    @Override
    public Object fetchTimelyForecast(Map<String, String> inputParam)
            throws BaseException {
        log.info("Entering fetchTimelyForecast");
        inputParam.put(Constants.UNITS, Units.valueOf(inputParam.get(Constants.UNITS).toUpperCase()).getApiUnits());
        WeatherDataList weatherDataList = commonUtils.get(weatherApiInUnits,inputParam);
        TimeWindowResponseList temperatureList = new TimeWindowResponseList();
        LinkedList tempList = new LinkedList();
        weatherDataList.getWeatherForecastedDataList().forEach(weatherForecastedData -> {
            TimeWindowResponse timeWindowResponse = new TimeWindowResponse();
            timeWindowResponse.setKey(weatherUtils.fetchTime(Long.valueOf(weatherForecastedData.getDate()),
                    weatherDataList.getCity().getTimezone()));
            timeWindowResponse.setTemperature(weatherForecastedData.getTemperature().getTemperature());
            timeWindowResponse.setWeatherIcon(weatherForecastedData.getWeather().getFirst().getWeatherIcon());
            tempList.add(timeWindowResponse);
        });
        temperatureList.setTimeWindowResponses(tempList);
        log.info("Exiting fetchTimelyForecast");
        return temperatureList;
    }

    /**
     * fetches daily forecast
     * @param inputParam inputParam
     * @return temperature list
     * @throws BaseException BaseException
     */
    @Cacheable(value = "DailyForecast", cacheManager = "cacheManager")
    @Override
    public Object fetchDailyForecast(Map<String, String> inputParam)
            throws BaseException {
        log.info("Entering fetchDailyForecast");
        inputParam.put(Constants.UNITS,Units.valueOf(inputParam.get(Constants.UNITS).toUpperCase()).getApiUnits());
        WeatherDataList weatherDataList = commonUtils.get(weatherApiInUnits,inputParam);
        List<TimeWindowResponse> response = new LinkedList<>();
        //grouping the data according to date
        Map<Object, List<WeatherForecastedData>> groupedData = weatherDataList.getWeatherForecastedDataList()
                .stream().collect(Collectors.groupingBy(
                        weatherForecastedData -> weatherForecastedData.getDateText().substring(0,10)));
        //iterating through the weather data
        weatherDataList.getWeatherForecastedDataList().forEach(forecastedData-> {
            String dateKey = forecastedData.getDateText().substring(0,10);
            List<WeatherForecastedData> weatherForecastedDataList = groupedData.get(
                    dateKey);
            if (weatherForecastedDataList != null) {
                if(weatherDataList.getCity().getTimezone()<0){
                    forecastedData = weatherForecastedDataList.getLast();
                }
                //calcuate average temperature
                double tempSum = weatherForecastedDataList.stream().mapToDouble(
                        w -> w.getTemperature().getTemperature()).sum();
                double avgTemp = tempSum / (weatherForecastedDataList.size());
                //populate response
                TimeWindowResponse timeWindowResponse = new TimeWindowResponse();
                timeWindowResponse.setKey(weatherUtils.fetchDay(forecastedData.getDate(),
                        weatherDataList.getCity().getTimezone()));
                timeWindowResponse.setTemperature(avgTemp);
                timeWindowResponse.setWeatherIcon((String) weatherUtils.fetchWeatherIcon(
                        weatherForecastedDataList,weatherDataList.getCity().getTimezone()));
                response.add(timeWindowResponse);
                //removing the data from groupedData
                groupedData.remove(dateKey);
            }
        });
        log.info("Exiting fetchDailyForecast");
        return response;
    }

    /**
     * fetches forecast for three days
     * @param inputParam inputParam
     * @return ThreeDayForecastResponse
     * @throws BaseException BaseException
     */
    @Override
    public Object fetchThreeDayForecast(Map<String, String> inputParam)
            throws BaseException {
        log.info("Entering fetchThreeDayForecast");
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
            if(!ObjectUtils.isEmpty(weatherForecastedDataList)) {
                if(weatherDataList.getCity().getTimezone()<0){
                    weatherForecastedData = weatherForecastedDataList.getLast();
                }
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
                weatherData.setMaxTemp(maxTemp.orElse(0.0));
                weatherData.setMinTemp(minTemp.orElse(0.0));
                City city = weatherDataList.getCity();
                weatherData.setDay(weatherUtils.fetchDay(weatherForecastedData.getDate(), city.getTimezone()));
                weatherData.setTime(weatherUtils.fetchTime(Long.valueOf(weatherForecastedData.getDate()),city.getTimezone()));
                weatherData.setDateText(weatherUtils.fetchDate(weatherForecastedData.getDate(),city.getTimezone()));
                weatherData.setWeatherIcon((String) weatherUtils.fetchWeatherIcon(weatherForecastedDataList,weatherDataList.getCity().getTimezone()));
                Optional<WeatherForecastedData> rain = weatherForecastedDataList.stream().filter(
                        w -> !ObjectUtils.isEmpty(w.getRain())).findAny();
                OptionalDouble wind =
                        weatherForecastedDataList.stream().mapToDouble(w -> w.getWind().getWindSpeed()).max();
                Units unit = Units.valueOf(inputParam.get(Constants.UNITS).toUpperCase());
                populateAdditionalFields(avgTemp, weatherData, rain, wind, unit);
                weatherDataLinkedList.add(weatherData);
                groupedData.remove(
                        weatherForecastedData.getDateText().substring(0, 10));
            }
        });
        response.setWeatherData(weatherDataLinkedList);
        response.setCountry(weatherDataList.getCity().getCountry());
        response.setCityName(weatherDataList.getCity().getCityName());
        log.info("Exiting fetchThreeDayForecast");
        return response;
    }

    /**
     * populates additional fields
     * @param avgTemp avgTemp
     * @param weatherData weatherData
     * @param rain rain
     * @param wind wind
     * @param unit unit
     */
    private void populateAdditionalFields(double avgTemp, WeatherData weatherData,
            Optional<WeatherForecastedData> rain, OptionalDouble wind,
            Units unit) {
        if (rain.isPresent()) {
            weatherData.setDescription("Carry umbrella");
        }
        if (avgTemp > unit.getThresholdTemp()) {
            weatherData.setDescription("Use sunscreen lotion");
        }
        if (wind.isPresent() && wind.getAsDouble() > unit.getThresholdWindSpeed()) {
            weatherData.setAdditionalDescription(
                    "It’s too windy, watch out!");
        }
        if (wind.isPresent() && wind.getAsDouble() > unit.getStormIndicator()) {
            weatherData.setAdditionalDescription(
                    "Don’t step out! A Storm is brewing!");
        }
    }
}
