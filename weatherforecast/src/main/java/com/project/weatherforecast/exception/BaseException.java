package com.project.weatherforecast.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;

@AllArgsConstructor
public class BaseException extends Throwable {
    private UUID uuid;
    private HttpStatus status;
    private String message;
}
