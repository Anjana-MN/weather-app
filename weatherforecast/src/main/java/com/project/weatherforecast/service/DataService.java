package com.project.weatherforecast.service;

import com.project.weatherforecast.config.ForecastTypeEnum;
import com.project.weatherforecast.exception.BaseException;

import java.util.Map;

public interface DataService {

    boolean isForecastType(ForecastTypeEnum forecastType);

    Object fetchData(Map<String, String> inputParam)
            throws BaseException;
}
