package com.project.weatherforecast.util;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CommonUtils {

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
