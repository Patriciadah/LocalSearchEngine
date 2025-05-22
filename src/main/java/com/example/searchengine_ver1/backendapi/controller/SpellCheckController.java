package com.example.searchengine_ver1.backendapi.controller;

import com.example.searchengine_ver1.backendapi.service.SpellingCorrectorService;
import com.example.searchengine_ver1.backendapi.spellcheck.DictionaryOnlyStrategy;
import com.example.searchengine_ver1.backendapi.spellcheck.NorvigCorrectionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Map;
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
    @GetMapping("/setStrategy")
    public void setStrategy(@RequestParam String type) {
        Map<String, Integer> wordCounts;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("src/main/resources/wordMap.ser"))) {
            wordCounts = (Map<String, Integer>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load wordMap.ser", e);
        }
        if ("norvig".equalsIgnoreCase(type)) {
            spellingCorrectorService.setStrategy(new NorvigCorrectionStrategy(wordCounts));
        } else if ("dictionary".equalsIgnoreCase(type)) {
            spellingCorrectorService.setStrategy(new DictionaryOnlyStrategy(wordCounts));
        }
    }

}
