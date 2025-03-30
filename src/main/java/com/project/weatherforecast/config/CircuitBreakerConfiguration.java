//package com.project.weatherforecast.config;
//
//import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
//import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@Slf4j
//public class CircuitBreakerConfiguration {
//
//    @Bean
//    public CircuitBreakerRegistry circuitBreakerRegistry() {
//        // Define the default configuration for your circuit breakers
//        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
//                .failureRateThreshold(50)  // Percentage of failures after which the circuit breaker will trip
//                .waitDurationInOpenState(java.time.Duration.ofMillis(1000))  // Time to wait before attempting to reset the circuit breaker
////                .ringBufferSizeInClosedState(100)  // Number of requests to consider before deciding to trip
////                .ringBufferSizeInHalfOpenState(10)  // Number of requests to consider in half-open state
//                .build();
//
//        // Return a CircuitBreakerRegistry with the default configuration
//        return CircuitBreakerRegistry.of(config);
//    }
//}
