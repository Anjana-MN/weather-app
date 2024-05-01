package com.project.weatherforecast.controller;

import com.project.weatherforecast.bean.Response;
import com.project.weatherforecast.bean.ThreeDayForecastResponse;
import com.project.weatherforecast.bean.TimeWindowResponse;
import com.project.weatherforecast.bean.TimeWindowResponseList;
import com.project.weatherforecast.constants.Constants;
import com.project.weatherforecast.exception.BaseException;
import com.project.weatherforecast.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/weather/forecast")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/threedays")
    @Operation( summary = "Fetch Forecast for Three Days",tags = "Forecast", method = Constants.GET)
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found") })
    public ResponseEntity<Object> getWeatherForecastForNextThreeDays(
            @Parameter(name = "count", description = "count")
            @RequestParam(value = "count", name = "count", required = false, defaultValue = "1")
                    String count,
            @Parameter(name = "city", description = "city", required = true)
            @RequestParam(value = "city", name = "city")
                    String city,
            @Parameter(name = "units", description = "units")
            @RequestParam(value = "units", name = "units", required = false, defaultValue = "celsius")
                    String units) throws BaseException {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put(Constants.COUNT, count);
        inputParam.put(Constants.CITY, city);
        inputParam.put(Constants.UNITS, units);
        ThreeDayForecastResponse responseList =
                (ThreeDayForecastResponse) weatherService.fetchThreeDayForecast(
                        inputParam);
        responseList.add(linkTo(methodOn(
                WeatherController.class).getWeatherForecastForNextThreeDays(
                count, city, units)).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/current")
    @Operation( summary = "Fetch Current Weather",tags = "Forecast", method = Constants.GET)
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found") })
    public ResponseEntity<Object> getCurrentWeatherData(
            @Parameter(name = "count", description = "count")
            @RequestParam(value = "count", name = "count", required = false, defaultValue = "1")
                    String count,
            @Parameter(name = "city", description = "city", required = true)
            @RequestParam(value = "city", name = "city")
                    String city,
            @Parameter(name = "units", description = "units")
            @RequestParam(value = "units", name = "units", required = false, defaultValue = "celsius")
                    String units) throws BaseException {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put(Constants.COUNT, count);
        inputParam.put(Constants.CITY, city);
        inputParam.put(Constants.UNITS, units);
        Response response =
                (Response) weatherService.fetchCurrentWeather(inputParam);
        response.add(
                linkTo(methodOn(WeatherController.class).getCurrentWeatherData(
                        count, city, units)).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/timely")
    @Operation( summary = "Fetch Timely Forecast",tags = "Forecast", method = Constants.GET)
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found") })
    public ResponseEntity<Object> getTimelyForecast(
            @Parameter(name = "count", description = "count")
            @RequestParam(value = "count", name = "count", defaultValue = "5")
                    String count,
            @Parameter(name = "city", description = "city", required = true)
            @RequestParam(value = "city", name = "city") String city,
            @Parameter(name = "units", description = "units")
            @RequestParam(value = "units", name = "units", defaultValue = "celsius")
                    String units) throws BaseException {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put(Constants.COUNT, count);
        inputParam.put(Constants.CITY, city);
        inputParam.put(Constants.UNITS, units);
        TimeWindowResponseList responseList =
                (TimeWindowResponseList) weatherService.fetchTimelyForecast(
                        inputParam);
        responseList.add(
                linkTo(methodOn(WeatherController.class).getTimelyForecast(
                        count, city, units)).withSelfRel());
        Map<String, Object> data = new HashMap<>();
        data.put("data", responseList);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/daily")
    @Operation( summary = "Fetch Daily Forecast",tags = "Forecast", method = Constants.GET)
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found") })
    public ResponseEntity<Object> getDailyForecast(
            @Parameter(name = "count", description = "count")
            @RequestParam(value = "count", name = "count", defaultValue = "25")
                    String count,
            @Parameter(name = "city", description = "city", required = true)
            @RequestParam(value = "city", name = "city") String city,
            @Parameter(name = "units", description = "units")
            @RequestParam(value = "units", name = "units", defaultValue = "celsius")
                    String units) throws BaseException {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put(Constants.COUNT, count);
        inputParam.put(Constants.CITY, city);
        inputParam.put(Constants.UNITS, units);
        Map<String, Object> data = new HashMap<>();
        List<TimeWindowResponse> responseList =
                (List<TimeWindowResponse>) weatherService.fetchDailyForecast(
                        inputParam);
        for (TimeWindowResponse response : responseList) {
            response.add(
                    linkTo(methodOn(WeatherController.class).getTimelyForecast(
                            count, city, units)).withSelfRel());
        }
        data.put("dailyForecast", responseList);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }
}
