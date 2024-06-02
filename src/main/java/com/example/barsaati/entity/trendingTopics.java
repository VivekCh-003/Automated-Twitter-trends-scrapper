package com.example.barsaati.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document("trendingTopics")
@Data
public class trendingTopics {

    @Id
    private String id;
    private List<String> trends;
    private LocalDateTime localDateTime;
    private String ipAddress;
}
