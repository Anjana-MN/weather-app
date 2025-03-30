package com.project.weatherforecast.circuitbreaker;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/circuitbreaker")
public class CircuitBreakerController {

    @Autowired
    CircuitBreakerService circuitBreakerService;

    @GetMapping("/test")
    public Object getService(){
        return circuitBreakerService.getDataFromService();
    }
}
