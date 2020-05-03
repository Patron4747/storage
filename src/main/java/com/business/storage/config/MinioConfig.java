package com.business.storage.config;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: mmustafin@context-it.ru
 * @created: 10.04.2020
 */
@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String minioURL;
    @Value("${minio.key.access}")
    private String accessKey;
    @Value("${minio.key.secret}")
    private String secretKey;

    @Bean
    public MinioClient getMinioClient() {
        try {
            return new MinioClient(minioURL, accessKey, secretKey);
        } catch (InvalidEndpointException e) {
            throw new RuntimeException("Error connecting to Minio Client: " + e.getMessage());
        } catch (InvalidPortException e) {
            throw new RuntimeException("Error connection to Minio Client: " + e.getMessage());
        }
    }
}
