package com.project.weatherforecast.service;

import com.project.weatherforecast.exception.BaseException;

import java.util.Map;

public interface DataService {

    Object fetchData(Map<String, String> inputParam)
            throws BaseException;
}
