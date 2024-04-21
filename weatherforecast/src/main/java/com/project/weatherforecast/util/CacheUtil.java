package com.project.weatherforecast.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class CacheUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    public List<String> getKeys() {
        RedisConnection redisConnection =redisTemplate.getConnectionFactory().getConnection();
        Set<byte[]> redisKeys =redisConnection.keys("*".getBytes(
                StandardCharsets.UTF_8));
        List<String> keysList =new ArrayList<>(10);
        Iterator<byte[]> it = redisKeys.iterator();
        while (it.hasNext()) {
            byte[] data = it.next();
            keysList.add(new String(data,0,data.length));
        }
        redisConnection.close();
        return  keysList;
    }

    public Object getValueForKey(String key) {
        List<Object> value = new ArrayList<>(10);
        RedisConnection redisConnection =redisTemplate.getConnectionFactory().getConnection();
        Set<byte[]> keys = redisConnection.keys(key.getBytes(StandardCharsets.UTF_8));
        for(byte[] bkey: keys){
            byte[] b = redisConnection.get(bkey);
            Object o = redisTemplate.getValueSerializer().deserialize(b);
            value.add(o);
        }
        redisConnection.close();
        return value;
    }
}
