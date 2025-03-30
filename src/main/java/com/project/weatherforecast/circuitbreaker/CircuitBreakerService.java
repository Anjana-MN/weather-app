package com.project.weatherforecast.circuitbreaker;

//import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class CircuitBreakerService {

//    @Autowired
//    private CircuitBreakerFallback circuitBreakerFallback;

//    @Autowired
//    private CircuitBreakerRegistry circuitBreakerRegistry;

//    @Autowired
//    public CircuitBreakerService(CircuitBreakerRegistry circuitBreakerRegistry) {
//        this.circuitBreakerRegistry = circuitBreakerRegistry;
//    }

    // This method is both cached and protected by a circuit breaker.
//    @Cacheable("dataCache")
    @CircuitBreaker(name = "backendService", fallbackMethod = "fallbackMethod")
    public List<Integer> getDataFromService() {
        // Simulate method that might fail (e.g., calling an external API)
//        double x = Math.random();
//        log.info("x is {}",x);
//        if (Math.random() > 0.5) {
//        io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("backendService");
//        log.info("circuit breaker is {}", circuitBreaker.getState());
        throw new RuntimeException("External service failure");
//        }
//        return "Fetched Data";
    }

//    // Fallback method will return a cached fallback value
//    public String fallbackMethod(Exception ex) {
//        log.error("In fallback method");
//        return "Fallback data";
//    }

    // Fallback method will return a cached fallback value
    public List<String> fallbackMethod(RuntimeException ex) {
        log.error("In fallback method");
        return Arrays.asList("Fallback data","dummy data");
    }
}
