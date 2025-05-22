package com.example.searchengine_ver1.backendapi.controller;

import com.example.searchengine_ver1.backendapi.service.SpellingCorrectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/spellcheck")
public class SpellCheckController {

    @Autowired
    private SpellingCorrectorService spellingCorrectorService;

    @GetMapping
    public String correctQuery(@RequestParam String query) {
        return Arrays.stream(query.split("\\s+"))
                .map(spellingCorrectorService::correct)
                .collect(Collectors.joining(" "));
    }
}
