package com.project.weatherforecast.bean.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Rain {
    /** predictedRain */
    @JsonProperty("3h")
    private String predictedRain;
}
