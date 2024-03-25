package com.project.weatherforecast.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Units {
    FAHRENHEIT("imperial"),CELSIUS("metric");

    private String apiUnits;
}
