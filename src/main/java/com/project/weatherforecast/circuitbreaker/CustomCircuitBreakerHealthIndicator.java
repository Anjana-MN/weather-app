//package com.project.weatherforecast.circuitbreaker;
//
//import io.github.resilience4j.circuitbreaker.CircuitBreaker;
//import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.actuate.health.Health;
//import org.springframework.boot.actuate.health.HealthIndicator;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CustomCircuitBreakerHealthIndicator implements HealthIndicator {
//
//    @Autowired
//    private CircuitBreakerRegistry circuitBreakerRegistry;
//
//    @Override
//    public Health health() {
//        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("weatherService");
//
//        if (circuitBreaker.getState() == CircuitBreaker.State.OPEN) {
//            return Health.down().withDetail("state", "OPEN").build();
//        } else if (circuitBreaker.getState() == CircuitBreaker.State.HALF_OPEN) {
//            return Health.status("HALF_OPEN").build();
//        } else {
//            return Health.up().withDetail("state", "CLOSED").build();
//        }
//    }
//}
