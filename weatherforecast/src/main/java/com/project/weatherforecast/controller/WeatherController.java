package com.project.weatherforecast.controller;

import com.project.weatherforecast.bean.Response;
import com.project.weatherforecast.bean.data.WeatherForecastedData;
import com.project.weatherforecast.exception.BaseException;
import com.project.weatherforecast.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/weather/forecast")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/data")
    public ResponseEntity<Object> getForecastForNextThreeDays(
            @RequestParam(value = "count", name="count", required = true,defaultValue="15") String count,
            @RequestParam(value = "city", name="city", required = true) String city
    ){
        Map<String,String> inputParam = new HashMap<>();
        inputParam.put("count",count);
        inputParam.put("city",city);
        Object responseList = weatherService.fetchWeatherData(inputParam);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/temperaturelist")
    public ResponseEntity<Object> getTemperatures(
            @RequestParam(value = "count", name="count", defaultValue="15") String count,
            @RequestParam(value = "city", name="city") String city,
            @RequestParam(value = "units", name="units", defaultValue="celsius") String units
    ) throws BaseException {
        Map<String,String> inputParam = new HashMap<>();
        inputParam.put("count",count);
        inputParam.put("city",city);
        inputParam.put("units",units);
        Object responseList = weatherService.fetchTemperatures(inputParam);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/daily")
    public ResponseEntity<Object> getDailyForecast(
            @RequestParam(value = "count", name="count", defaultValue="15") String count,
            @RequestParam(value = "city", name="city") String city,
            @RequestParam(value = "units", name="units", defaultValue="celsius") String units
    ) throws BaseException {
        Map<String,String> inputParam = new HashMap<>();
        inputParam.put("count",count);
        inputParam.put("city",city);
        inputParam.put("units",units);
        Object responseList = weatherService.fetchDailyForeCast(inputParam);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }
}
