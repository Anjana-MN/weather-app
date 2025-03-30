package com.project.weatherforecast.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;


@Getter
@Setter
public class BaseException extends RuntimeException {
    /** message */
    private String message;
    /** httpStatusCode */
    private HttpStatusCode httpStatusCode;

    /**
     * Base Exception
     * @param message message
     * @param httpStatusCode httpStatusCode
     */
    public BaseException(String message, HttpStatusCode httpStatusCode){
        super(message);
        this.message = message;
        this.httpStatusCode=httpStatusCode;
    }

    /**
     * Base Exception
     * @param message message
     */
    public BaseException(String message){
        super(message);
        this.message=message;
    }
}
