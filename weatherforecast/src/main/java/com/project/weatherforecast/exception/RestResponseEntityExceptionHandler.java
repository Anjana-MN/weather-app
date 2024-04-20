package com.project.weatherforecast.exception;

import com.project.weatherforecast.bean.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends
        ResponseEntityExceptionHandler {

    @Autowired
    private ErrorResponse errorResponse;

    public RestResponseEntityExceptionHandler() {
        super();
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> handleBaseException(BaseException ex, WebRequest request){
        return handleExceptionInternal(ex, errorResponse.constructResponse(
                ex.getMessage(), ex.getHttpStatusCode()),new HttpHeaders(),
                ex.getHttpStatusCode(), request);
    }
}
