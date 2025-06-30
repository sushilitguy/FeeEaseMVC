package com.softmania.feeease.component;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AwsUrlManager {
    private final Map<String, PresignedUrl> awsUrlMap;

    public AwsUrlManager(Map<String, PresignedUrl> awsUrlMap) {
        this.awsUrlMap = awsUrlMap;
    }

    public void addUrl(String key, PresignedUrl url) {
        awsUrlMap.put(key, url);
    }

    public PresignedUrl getExistingUrl(String key) {
        return awsUrlMap.get(key);
    }
}
