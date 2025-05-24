package com.example.searchengine_ver1.backendapi.controller;

import com.example.searchengine_ver1.backendapi.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/suggest")
public class SuggestionController {

    @Autowired
    private SuggestionService suggestionService;

    @GetMapping
    public Map<String, List<String>> getSuggestion() {
        return suggestionService.getStructuredSuggestions();
    }

}
