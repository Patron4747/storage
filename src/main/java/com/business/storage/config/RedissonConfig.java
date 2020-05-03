package com.business.storage.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: mmustafin@context-it.ru
 * @created: 12.04.2020
 */
@Configuration
public class RedissonConfig {

    @Value("${redis.host}")
    private String host;
    @Value("${redis.port}")
    private String port;

    @Bean
    public RedissonClient getRedissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(host+":"+port);

        return Redisson.create(config);
    }
}
