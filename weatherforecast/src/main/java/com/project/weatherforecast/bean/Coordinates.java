package com.project.weatherforecast.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Coordinates implements Serializable {
    /** latitude */
    private String latitude;
    /** longitude */
    private String longitude;
}
