package com.project.weatherforecast.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;


@Getter
@Setter
public class BaseException extends RuntimeException {

    private String message;
    private HttpStatusCode httpStatusCode;

    public BaseException(String message, HttpStatusCode httpStatusCode){
        super(message);
        this.message = message;
        this.httpStatusCode=httpStatusCode;
    }

    public BaseException(String message){
        super(message);
        this.message=message;
    }
}
