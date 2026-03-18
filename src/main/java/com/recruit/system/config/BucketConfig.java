package com.recruit.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BucketConfig {

    @Value("${aws.bucketName}")
    private String bucketName;

    public String getBucketName() {
        return bucketName;
    }
}