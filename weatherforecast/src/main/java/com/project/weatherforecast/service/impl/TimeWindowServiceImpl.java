package com.project.weatherforecast.service.impl;

import com.project.weatherforecast.bean.Units;
import com.project.weatherforecast.bean.data.WeatherDataList;
import com.project.weatherforecast.config.ForecastTypeEnum;
import com.project.weatherforecast.exception.BaseException;
import com.project.weatherforecast.service.DataService;
import com.project.weatherforecast.util.CommonUtils;
import com.project.weatherforecast.util.WeatherUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@Slf4j
public class TimeWindowServiceImpl implements DataService {

    @Autowired
    private WeatherUtils weatherUtils;

    @Autowired
    private CommonUtils commonUtils;

    @Value("${weather.units.api}")
    private String weatherApiInUnits;

    @Override
    public boolean isForecastType(ForecastTypeEnum forecastType) {
        return ForecastTypeEnum.TIMELY.equals(forecastType);
    }

    @Override
    @Cacheable(cacheNames = "TimelyForecast", cacheManager = "cacheManager")
    public Object fetchData(Map<String, String> inputParam)
            throws BaseException {
        inputParam.put("units",Units.valueOf(inputParam.get("units").toUpperCase()).getApiUnits());
        WeatherDataList weatherDataList = commonUtils.get(weatherApiInUnits,inputParam);
        return weatherUtils.fetchTempList(weatherDataList);
    }
}
