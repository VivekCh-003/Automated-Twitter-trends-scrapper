package com.example.barsaati.service;

import com.example.barsaati.entity.trendingTopics;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface trendingTopicsService {
    trendingTopics login() throws Exception;

    String getLatestTrendingTopicsAsJson() throws JsonProcessingException;
}
