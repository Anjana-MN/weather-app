package com.project.weatherforecast.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.weatherforecast.bean.data.WeatherDataList;
import com.project.weatherforecast.bean.data.WeatherForecastedData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class WeatherUtilsTest {

    @InjectMocks
    private WeatherUtils weatherUtils;

    @Test
    public void testProcessWeatherResponse() throws IOException {
        WeatherDataList weatherDataList = new ObjectMapper()
                .readValue(new File("src/test/resources/json/MockWeatherDataList.json"),
                        WeatherDataList.class);
        assertNotNull(weatherUtils.processWeatherResponse(weatherDataList.getWeatherForecastedDataList().get(0), (List<WeatherForecastedData>) weatherDataList));
    }

    @Test
    public void testFetchTime() {
        assertNotNull(weatherUtils.fetchTime(Long.valueOf(10),0));
    }

    @Test
    public void testFetchDay() {
        assertNotNull(weatherUtils.fetchDay("1078456",0));
    }

    @Test
    public void testFetchDate() {
        assertNotNull(weatherUtils.fetchDate("1078456",0));
    }

    @Test
    public void testFetchWeatherIcon() throws IOException {
        WeatherDataList weatherDataList = new ObjectMapper()
                .readValue(new File("src/test/resources/json/MockWeatherDataList.json"),
                        WeatherDataList.class);
        assertNotNull(weatherUtils.fetchWeatherIcon(weatherDataList.getWeatherForecastedDataList(),0));
    }
}
