package com.project.weatherforecast.service;

import com.project.weatherforecast.exception.BaseException;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {

    private final Map<String, WeatherForecastDataProcessor> weatherProcessors;

    @Autowired
    public WeatherService(CurrentWeatherForecastDataProcessorImpl currentWeatherForecastDataProcessor,
                          DailyWeatherForecastDataProcessor dailyWeatherForecastDataProcessor,
                          ThreeDayWeatherForecastDataProcessor threeDayWeatherForecastDataProcessor,
                          TimelyForecastDataProcessor timelyForecastDataProcessor){
        weatherProcessors = new HashMap<>();
        weatherProcessors.put("current", currentWeatherForecastDataProcessor);
        weatherProcessors.put("daily", dailyWeatherForecastDataProcessor);
        weatherProcessors.put("threeDay", threeDayWeatherForecastDataProcessor);
        weatherProcessors.put("timely", timelyForecastDataProcessor);
    }

    public Object getWeatherData(String key, Map<String,String> inputParam){
        WeatherForecastDataProcessor processor = weatherProcessors.get(key);
        if(processor == null){
            return new IllegalArgumentException("Invalid processor");
        }
        return processor.fetchWeather(inputParam);
    }
}
