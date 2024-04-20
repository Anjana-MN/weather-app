package com.project.weatherforecast.util;

import com.project.weatherforecast.bean.data.WeatherDataList;
import com.project.weatherforecast.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    public WeatherDataList get(String url, Map<String,String> inputParam)
            throws BaseException {
        WeatherDataList weatherDataList;
        String queryParams = buildQuery(inputParam,weatherQueryMap);
        url = url.concat(queryParams);
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
        inputParam.entrySet().forEach((entry)->{
            if(queryMap.containsKey(entry.getKey())){
                builder.append(queryMap.get(entry.getKey()).concat(entry.getValue()));
            }
        });
        return String.valueOf(builder);
    }
}
