package com.project.weatherforecast.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class TimeWindowResponse extends
        RepresentationModel<TimeWindowResponse> {

    private String key;
    private Double temperature;
    private String weatherIcon;
}
