package com.project.weatherforecast.service.impl;

import com.project.weatherforecast.bean.Response;
import com.project.weatherforecast.bean.WeatherData;
import com.project.weatherforecast.bean.data.City;
import com.project.weatherforecast.bean.data.WeatherDataList;
import com.project.weatherforecast.bean.data.WeatherForecastedData;
import com.project.weatherforecast.constants.Constants;
import com.project.weatherforecast.exception.BaseException;
import com.project.weatherforecast.service.WeatherForecastDataProcessor;
import com.project.weatherforecast.util.WeatherUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
@Service
public class CurrentWeatherForecastDataProcessorImpl implements WeatherForecastDataProcessor {

    @Autowired
    private WeatherUtils weatherUtils;

    /**
     * fetches the current weather
     * @param inputParam inputParam
     * @return response
     * @throws BaseException BaseException
     */
    @Override
    public Object fetchWeather(Map<String, String> inputParam)
            throws BaseException {
        log.info("fetching current weather for {} in {} units", inputParam.get(Constants.CITY), inputParam.get(Constants.UNITS));
        Response response = new Response();
        WeatherDataList weatherResponse = weatherUtils.getWeatherDataList(inputParam);
        if(weatherResponse.getCod().equalsIgnoreCase(String.valueOf(HttpStatusCode.valueOf(200)))) {
            try {
                City city = weatherResponse.getCity();
                response.setSunRise(weatherUtils.fetchTime(city.getSunRise(), city.getTimezone()));
                response.setSunSet(weatherUtils.fetchTime(city.getSunSet(), city.getTimezone()));
                BeanUtils.copyProperties(city.getCoordinates(), response.getCoordinates());
                BeanUtils.copyProperties(city, response);
                WeatherForecastedData weatherForecastedData = weatherResponse.getWeatherForecastedDataList().getFirst();
                WeatherData weatherData = weatherUtils.processWeatherResponse(weatherForecastedData, new ArrayList<>() {{
                    add(weatherForecastedData);
                }});
                weatherData = weatherUtils.setDateTime(weatherData, weatherForecastedData, city, new ArrayList<>() {{
                    add(weatherForecastedData);
                }});
                BeanUtils.copyProperties(city, weatherData);
                response.setWeatherData(weatherData);
            } catch (Exception e) {
                log.error("An unexpected error occurred: {}", e.getMessage());
                throw new BaseException("Unexpected error", HttpStatusCode.valueOf(500));
            }
        }
        return response;
    }
}
