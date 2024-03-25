package com.project.weatherforecast.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeWindowResponse {

    private String dateText;
    private Double temperature;
    private String weatherIcon;
}
