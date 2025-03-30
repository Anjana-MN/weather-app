package com.project.weatherforecast.util;

import com.project.weatherforecast.bean.data.WeatherDataList;
import com.project.weatherforecast.exception.BaseException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class CommonUtils {
    /** The restTemplate */
    @Autowired
    private RestTemplate restTemplate;
    /** The weatherQueryMap */
    @Value("#{${weather.query.map}}")
    private Map<String,String> weatherQueryMap;
    /** The appId */
    @Value("${weather.appid}")
    private String appId;

    /**
     * fetches weather data
     * @param url url
     * @param inputParam inputParam
     * @return weatherDataList
     * @throws BaseException BaseException
     */
    @Cacheable(value = "WeatherData", cacheManager = "cacheManager", key = "'city='+#inputParam.get('city')+'&units='+#inputParam.get('units')+'&count='+#inputParam.get('count')", condition = "#weatherDataList == null || #weatherDataList.size() == 0")
    @CircuitBreaker(name = "weatherService", fallbackMethod = "weatherDataFallback")
    public WeatherDataList get(String url, Map<String,String> inputParam)
            throws BaseException {
        WeatherDataList weatherDataList;
        inputParam.put("appId",appId);
        String queryParams = buildQuery(inputParam,weatherQueryMap);
        url = url.concat(queryParams);
        log.info("Calling weather data api: {}", url);
        try{
            weatherDataList = restTemplate.getForObject(url,WeatherDataList.class);
        }catch (HttpClientErrorException e){
            log.error("Exception: {}{}{}", UUID.randomUUID(), e.getStatusCode(),
                    e.getMessage());
            JSONObject res = new JSONObject(e.getResponseBodyAsString());
            throw new BaseException(res.optString("message"),
                    HttpStatusCode.valueOf(res.optInt("cod")));
        } catch (SpelEvaluationException e) {
            throw new BaseException("SpEL evaluation error occurred", HttpStatusCode.valueOf(500));
        }
        return weatherDataList;
    }

    public WeatherDataList weatherDataFallback(Exception ex) {
        log.error("In fallback method {}",ex.toString());
        WeatherDataList weatherDataList = new WeatherDataList();
        String exString = ex.toString();
        weatherDataList.setMessage(ex.getMessage());
        if(exString.contains("CallNotPermittedException")  || exString.contains("SpelEvaluationException")){
            weatherDataList.setCod(HttpStatusCode.valueOf(500).toString());
        } else {
            weatherDataList.setCod(String.valueOf(((BaseException) ex).getHttpStatusCode()));
        }
        weatherDataList.setWeatherForecastedDataList(new LinkedList<>());
        return weatherDataList;
    }

    /**
     * builds query param
     * @param inputParam inputParam
     * @param queryMap queryMap
     * @return queryParam
     */
    public String buildQuery(Map<String, String> inputParam,
            Map<String, String> queryMap) {
        StringBuilder builder = new StringBuilder();
        log.info("Building query param");
        inputParam.forEach((key, value) -> {
            if (queryMap.containsKey(key)) {
                builder.append(queryMap.get(key).concat(value));
            }
        });
        return String.valueOf(builder);
    }
}
