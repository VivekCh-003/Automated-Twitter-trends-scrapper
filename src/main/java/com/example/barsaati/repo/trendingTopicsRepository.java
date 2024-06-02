package com.example.barsaati.repo;

import com.example.barsaati.entity.trendingTopics;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface trendingTopicsRepository extends MongoRepository<trendingTopics,String> {
    trendingTopics findTopByOrderByLocalDateTimeDesc();
}
