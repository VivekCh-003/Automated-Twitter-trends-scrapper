package com.example.barsaati.controller;

import com.example.barsaati.entity.trendingTopics;
import com.example.barsaati.exception.LoginException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.barsaati.service.trendingTopicsService;

@Controller
public class trendingTopicsController {
    @Autowired
    private trendingTopicsService trendingTopicsService;

    @GetMapping("/scrape")
    public String login(Model theModel) throws Exception {
        trendingTopics trendingTopics = trendingTopicsService.login();
        String trendingTopicsJson = "";
        try {
            trendingTopicsJson = trendingTopicsService.getLatestTrendingTopicsAsJson();
        } catch (JsonProcessingException e) {
            theModel.addAttribute("error", "Failed to process JSON data.");
            e.printStackTrace();
        } catch (Exception e) {
            theModel.addAttribute("error", "Failed to login: " + e.getMessage());
            e.printStackTrace();
        }
        theModel.addAttribute("trendingTopics",trendingTopics);
        theModel.addAttribute("trendingTopicsJson",trendingTopicsJson);
        return "home";
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }
}
