package com.example.searchengine_ver1.backendapi.controller;

import com.example.searchengine_ver1.backendapi.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suggest")
public class SuggestionController {

    @Autowired
    private SuggestionService suggestionService;

    @GetMapping
    public String getSuggestion() {
        return suggestionService.suggest();
    }
}
