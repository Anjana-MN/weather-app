package com.project.weatherforecast.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Units {
    //metric: celsius, meters per sec
    //imperial: fahrenheit, miles per hour
    FAHRENHEIT("imperial",104, 22.37,82.76),CELSIUS("metric",40,10.00, 37.00);

    /** apiUnits */
    private String apiUnits;
    /** thresholdTemp */
    private Integer thresholdTemp;
    /** thresholdWindSpeed */
    private Double thresholdWindSpeed;
    /** stormIndicator */
    private Double stormIndicator;
}
