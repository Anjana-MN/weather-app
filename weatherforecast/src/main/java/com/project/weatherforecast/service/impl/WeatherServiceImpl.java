package com.project.weatherforecast.service.impl;

import com.project.weatherforecast.config.ForecastTypeEnum;
import com.project.weatherforecast.config.ForecastTypeStrategyFactory;
import com.project.weatherforecast.exception.BaseException;
import com.project.weatherforecast.service.DataService;
import com.project.weatherforecast.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private ForecastTypeStrategyFactory factory;

    @Override
    public Object fetchWeatherData(Map<String, String> inputParam,
            ForecastTypeEnum forecastTypeEnum) throws BaseException {
        DataService forecastTypeService = factory.
                getForecastTypeStrategyServiceMap().get(forecastTypeEnum);
        return forecastTypeService.fetchData(inputParam);
    }
}
