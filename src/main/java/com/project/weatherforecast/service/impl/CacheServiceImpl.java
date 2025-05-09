package com.project.weatherforecast.service.impl;

import com.project.weatherforecast.exception.BaseException;
import com.project.weatherforecast.util.CacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CacheServiceImpl {
    /** The cacheUtil */
    @Autowired
    private CacheUtil cacheUtil;

    /**
     * get cache keys
     * @return list of keys
     * @throws BaseException BaseException
     */
    public Object getKeys() throws BaseException {
        Map<String, Object> output = new ConcurrentHashMap<>();
        try {
            List<String> keys = cacheUtil.getKeys();
            output.put("data", keys);
        }
        catch(Exception e) {
            throw new BaseException(e.getMessage());
        }
        return output;
    }

    /**
     * fetches the value for a cache key
     * @param data data
     * @return cahce value
     * @throws BaseException BaseException
     */
    public Object getValueForKey(Map<String,Object> data) throws BaseException{
        Map<String, Object> output = new ConcurrentHashMap<>();
        String key = (String) data.get("key");
        try{
            List<Object> values = (List<Object>) cacheUtil.getValueForKey(key);
            output.put("values", values);
        } catch (Exception e){
            throw new BaseException(e.getMessage());
        }
        return output;
    }
}
