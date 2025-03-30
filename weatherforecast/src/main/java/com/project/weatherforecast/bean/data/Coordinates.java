package com.project.weatherforecast.bean.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Coordinates {
    /** latitude */
    @JsonProperty("lat")
    private String latitude;
    /** longitude */
    @JsonProperty("lon")
    private String longitude;
}
