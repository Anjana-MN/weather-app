package com.project.weatherforecast.service;

import java.util.Map;

public interface CacheService {

    /**
     * fetches cache keys
     * @return list of cache keys
     */
    Object getKeys();

    /**
     * fetches cache value for a key
     * @param data data
     * @return cache value
     */
    Object getValueForKey(Map<String,Object> data);
}
