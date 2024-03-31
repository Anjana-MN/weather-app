package com.project.weatherforecast.config;

import com.project.weatherforecast.service.DataService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.EnumMap;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class ForecastTypeStrategyFactory {

    private Map<ForecastTypeEnum, DataService> forecastTypeStrategyServiceMap = new EnumMap(ForecastTypeEnum.class);

    @Autowired
    private List<DataService> forecastTypeStrategyList;

    @PostConstruct
    public void forecastTypeStrategyMap(){
        Arrays.stream(ForecastTypeEnum.values()).forEach(forecastTypeEnum -> {
            forecastTypeStrategyList.stream().forEach(forecastTypeStrategy->{
            if(forecastTypeStrategy.isForecastType(forecastTypeEnum)){
                forecastTypeStrategyServiceMap.put(forecastTypeEnum,forecastTypeStrategy);
            }
            });
        });
    }

    public Map<ForecastTypeEnum, DataService> getForecastTypeStrategyServiceMap() {
        return forecastTypeStrategyServiceMap;
    }
}
