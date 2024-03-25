package com.project.weatherforecast.bean.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Rain {
    @JsonProperty("3h")
    private String predictedRain;
}
