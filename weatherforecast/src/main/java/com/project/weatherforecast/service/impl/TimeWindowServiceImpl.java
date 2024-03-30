package com.project.weatherforecast.service.impl;

import com.project.weatherforecast.bean.Units;
import com.project.weatherforecast.bean.data.WeatherDataList;
import com.project.weatherforecast.exception.BaseException;
import com.project.weatherforecast.service.DataService;
import com.project.weatherforecast.util.WeatherUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class TimeWindowServiceImpl implements DataService {

    @Autowired
    private WeatherUtils weatherUtils;

    @Value("${weather.units.api}")
    private String weatherApiInUnits;

    @Override
    public Object fetchData(Map<String, String> inputParam)
            throws BaseException {
        Map<String,Object> map = new HashMap<>();
        inputParam.put("units",Units.valueOf(inputParam.get("units").toUpperCase()).getApiUnits());
        WeatherDataList weatherDataList = weatherUtils.get(weatherApiInUnits,inputParam);
        map.put("data",weatherUtils.fetchTempList(weatherDataList));
        return map;
    }
}
