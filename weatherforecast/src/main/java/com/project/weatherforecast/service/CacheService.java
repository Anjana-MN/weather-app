package com.project.weatherforecast.service;

import java.util.Map;

public interface CacheService {

    Object getKeys();

    Object getValueForKey(Map<String,Object> data);
}
