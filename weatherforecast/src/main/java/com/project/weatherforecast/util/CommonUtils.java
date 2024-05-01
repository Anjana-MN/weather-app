package com.project.weatherforecast.util;

import com.project.weatherforecast.bean.data.WeatherDataList;
import com.project.weatherforecast.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class CommonUtils {

    @Autowired
    private RestTemplate restTemplate;

    @Value("#{${weather.query.map}}")
    private Map<String,String> weatherQueryMap;

    @Cacheable(value = "WeatherData", cacheManager = "cacheManager", key = "'city='+#inputParam.get('city')+'&units='+#inputParam.get('units')+'&count='+#inputParam.get('count')")
    public WeatherDataList get(String url, Map<String,String> inputParam)
            throws BaseException {
        WeatherDataList weatherDataList;
        String queryParams = buildQuery(inputParam,weatherQueryMap);
        String appId = System.getenv("APP_ID");
        url = url.concat(appId);
        url = url.concat(queryParams);
        log.info("Calling weather data api: {}", url);
        try{
            weatherDataList = restTemplate.getForObject(url,WeatherDataList.class);
        }catch (HttpClientErrorException e){
            log.info("Exception: {}{}{}", UUID.randomUUID(), e.getStatusCode(),
                    e.getMessage());
            JSONObject res;
            res = new JSONObject(e.getResponseBodyAsString());
            throw new BaseException(res.optString("message"),
                    HttpStatusCode.valueOf(res.optInt("cod")));
        }
        return weatherDataList;
    }

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
