package com.project.weatherforecast.controller;

import com.project.weatherforecast.constants.Constants;
import com.project.weatherforecast.exception.BaseException;
import com.project.weatherforecast.service.impl.CacheServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cache")
public class CacheController {

    /** The cacheService */
    @Autowired
    private CacheServiceImpl cacheService;

    /**
     * fetches cache keys
     * @return response object
     * @throws BaseException BaseException
     */
    @GetMapping("")
    @Operation( summary = "Fetch Cache Keys",tags = "Cache", method = Constants.GET)
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found") })
    public ResponseEntity<Object> fetchCacheKeys() throws BaseException {
        return ResponseEntity.status(HttpStatus.OK).body(cacheService.getKeys());
    }

    /**
     * fetches the cache value for given key
     * @param key key
     * @return response object
     * @throws BaseException BaseException
     */
    @GetMapping("/{key}")
    @Operation( summary = "Fetch Cache Value For a Key",tags = "Cache", method = Constants.GET)
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found") })
    public ResponseEntity<Object> fetchCacheValue(
            @Parameter(name = "key", description = "key", required = true)
            @PathVariable(value = "key", name = "key") String key
    ) throws BaseException {
        Map<String,Object> data = new HashMap<>();
        data.put("key", key);
        return ResponseEntity.status(HttpStatus.OK).body(cacheService.getValueForKey(data));
    }
}
