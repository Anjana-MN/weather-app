package com.project.weatherforecast.bean;

import org.json.JSONObject;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

@Component
public class ErrorResponse {

    /**
     * The error response
     */
    public ErrorResponse() {
        super();
    }

    /**
     * constructs error response
     * @param message message
     * @param httpStatusCode httpStatusCode
     * @return error
     */
    public String constructResponse(String message, HttpStatusCode httpStatusCode){
        final JSONObject error = new JSONObject();
        final JSONObject statusInfo = new JSONObject();
        statusInfo.put("statusCode",String.valueOf(httpStatusCode));
        statusInfo.put("message",String.valueOf(message));
        error.put("error",statusInfo);
        return error.toString();
    }
}
