package com.project.weatherforecast.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class TimeWindowResponseList
        extends RepresentationModel<TimeWindowResponseList>
        implements Serializable {
    private List<TimeWindowResponse> timeWindowResponses;

}
