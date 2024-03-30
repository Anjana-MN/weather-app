package com.project.weatherforecast.controller;

import com.project.weatherforecast.bean.Response;
import com.project.weatherforecast.bean.TimeWindowResponse;
import com.project.weatherforecast.exception.BaseException;
import com.project.weatherforecast.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/weather/forecast")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/data")
    public ResponseEntity<Object> getForecastForNextThreeDays(
            @RequestParam(value = "count", name="count", required = false,defaultValue="15") String count,
            @RequestParam(value = "city", name="city", required = true) String city
    ) throws BaseException {
        Map<String,String> inputParam = new HashMap<>();
        inputParam.put("count",count);
        inputParam.put("city",city);
        Response responseList =
                (Response) weatherService.fetchWeatherData(inputParam);
        responseList.add(linkTo(methodOn(WeatherController.class).
                getForecastForNextThreeDays(count,city)).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/temperaturelist")
    public ResponseEntity<Object> getTemperatures(
            @RequestParam(value = "count", name="count", defaultValue="5") String count,
            @RequestParam(value = "city", name="city") String city,
            @RequestParam(value = "units", name="units", defaultValue="celsius") String units
    ) throws BaseException {
        Map<String,String> inputParam = new HashMap<>();
        inputParam.put("count",count);
        inputParam.put("city",city);
        inputParam.put("units",units);
        List<TimeWindowResponse> responseList =
                (List<TimeWindowResponse>) weatherService.fetchTemperatures(inputParam);
        for(TimeWindowResponse response: responseList) {
            response.add(
                    linkTo(methodOn(WeatherController.class).getTemperatures(
                            count, city, units)).withSelfRel());
        }
        Map<String,Object> data = new HashMap<>();
        data.put("data",responseList);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/daily")
    public ResponseEntity<Object> getDailyForecast(
            @RequestParam(value = "count", name="count", defaultValue="25") String count,
            @RequestParam(value = "city", name="city") String city,
            @RequestParam(value = "units", name="units", defaultValue="celsius") String units
    ) throws BaseException {
        Map<String,String> inputParam = new HashMap<>();
        inputParam.put("count",count);
        inputParam.put("city",city);
        inputParam.put("units",units);
        Map<String,Object> data = new HashMap<>();
        List<TimeWindowResponse> responseList =
                (List<TimeWindowResponse>) weatherService.fetchDailyForeCast(inputParam);
        for(TimeWindowResponse response: responseList) {
            response.add(
                    linkTo(methodOn(WeatherController.class).getTemperatures(
                            count, city, units)).withSelfRel());
        }
        data.put("dailyForecast",responseList);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }
}
