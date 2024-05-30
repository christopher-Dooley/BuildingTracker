package com.example.BuildingTracker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class BuildingTrackerConfiguration {

    @Bean
    public HttpClient client() {
        return HttpClient.newBuilder().build();
    }

}
