package com.project.weatherforecast.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@Getter
@Setter
public class TimeWindowResponse extends
        RepresentationModel<TimeWindowResponse> implements Serializable {

    private String key;
    private Double temperature;
    private String weatherIcon;
}
