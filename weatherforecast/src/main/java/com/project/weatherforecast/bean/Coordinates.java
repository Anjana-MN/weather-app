package com.project.weatherforecast.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Coordinates implements Serializable {
    private String latitude;
    private String longitude;
}
