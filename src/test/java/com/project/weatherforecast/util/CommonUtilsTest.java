package com.project.weatherforecast.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.weatherforecast.bean.data.WeatherDataList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class CommonUtilsTest {

    @InjectMocks
    private CommonUtils commonUtils;

    @Mock
    private RestTemplate restTemplate;

    Map<String, String> weatherQueryMap;
    Map<String, String> queryMap;
    @BeforeEach
    public void setup() {
        weatherQueryMap = new HashMap<>();
        weatherQueryMap.put("city","&q=");
        ReflectionTestUtils.setField(commonUtils,"weatherQueryMap",weatherQueryMap);
        ReflectionTestUtils.setField(commonUtils,"appId","appId");
    }

    @Test
    public void testGet() throws IOException {
        WeatherDataList weatherDataList = new ObjectMapper()
                .readValue(new File("src/test/resources/json/MockWeatherDataList.json"),
                        WeatherDataList.class);
        Map<String,String> inputParam = new HashMap<>();
        inputParam.put("city","Bangalore");
        when(restTemplate.getForObject(
                anyString(),any()))
                .thenReturn(weatherDataList);
        assertNotNull(commonUtils.get(
                "/api.openweathermap.org/data/2.5/forecast?appId=",inputParam));
    }

    @Test
    public void testGetResilience4j() throws IOException {
        WeatherDataList weatherDataList = new ObjectMapper()
                .readValue(new File("src/test/resources/json/MockWeatherDataList.json"),
                        WeatherDataList.class);
        Map<String,String> inputParam = new HashMap<>();
        inputParam.put("city","Bangalore");
        when(restTemplate.getForObject(
                anyString(),any()))
                .thenThrow(new RuntimeException());
        assertThrows(Exception.class, ()->commonUtils.get(
                "/api.openweathermap.org/data/2.5/forecast?appId=",inputParam));
    }

    @Test
    public void testBuildQuery() {
        Map<String,String> inputParam = new HashMap<>();
        inputParam.put("count","10");
        inputParam.put("city","Bangalore");
        inputParam.put("units","celsius");
        assertNotNull(commonUtils.buildQuery(inputParam,weatherQueryMap));
    }
}
