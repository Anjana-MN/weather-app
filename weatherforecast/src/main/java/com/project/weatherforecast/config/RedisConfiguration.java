package com.project.weatherforecast.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import java.time.Duration;

@Configuration
public class RedisConfiguration {

    /**
     * The redisTimeToLive.
     */
    @Value("${redis.cache.time-to-live}")
    private long redisCacheTimeToLive;

    /**
     * Redis template.
     *
     * @param factory
     *            the factory
     * @return the redis template
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(Object.class);
        StringRedisSerializer redisSerializer = new StringRedisSerializer();
        template.setDefaultSerializer(redisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setConnectionFactory(factory);
        return template;
    }

    @Bean(name = "cacheManager")
    @Primary
    public CacheManager cacheManager(RedisConnectionFactory factory){
        RedisCacheConfiguration rc = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(redisCacheTimeToLive))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        RedisSerializer.json()));
        return RedisCacheManager.builder(factory).cacheDefaults(rc).build();

    }
}
