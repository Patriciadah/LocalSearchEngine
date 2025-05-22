package com.example.searchengine_ver1.backendapi.service;

import com.example.searchengine_ver1.backendapi.spellcheck.*;

import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SpellingCorrectorService {

    private final Map<String, Integer> wordCounts;
    private SpellingCorrectionStrategy strategy;

    public SpellingCorrectorService() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("src/main/resources/wordMap.ser"))) {
            wordCounts = (Map<String, Integer>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load wordMap.ser", e);
        }

        // Choose a strategy
        this.strategy = new NorvigCorrectionStrategy(wordCounts);
        // switch to another one:
        // this.strategy = new DictionaryOnlyStrategy(wordCounts);
    }

    public void setStrategy(SpellingCorrectionStrategy strategy) {
        this.strategy = strategy;
    }

    public String correct(String word) {
        return strategy.correct(word);
    }

    public String correctQuery(String query) {
        return Arrays.stream(query.split("\\s+"))
                .map(this::correct)
                .collect(Collectors.joining(" "));
    }
}
