package com.project.weatherforecast.bean.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Coordinates {

    @JsonProperty("lat")
    private String latitude;

    @JsonProperty("lon")
    private String longitude;
}
