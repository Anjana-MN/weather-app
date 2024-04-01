package com.project.weatherforecast.controller;

import com.project.weatherforecast.exception.BaseException;
import com.project.weatherforecast.service.impl.CacheServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cache")
public class CacheController {

    @Autowired
    private CacheServiceImpl cacheService;

    @GetMapping("")
    public ResponseEntity<Object> fetchCacheKeys() throws BaseException {
        return ResponseEntity.status(HttpStatus.OK).body(cacheService.getKeys());
    }

    @GetMapping("/{key}")
    public ResponseEntity<Object> fetchCacheKeys(
            @PathVariable(value = "key", name = "key") String key
    ) throws BaseException {
        Map<String,Object> data = new HashMap<>();
        data.put("key", key);
        return ResponseEntity.status(HttpStatus.OK).body(cacheService.getValueForKey(data));
    }
}
