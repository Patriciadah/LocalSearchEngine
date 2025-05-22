package com.example.searchengine_ver1.backendapi.spellcheck;

import java.util.Map;

public class DictionaryOnlyStrategy implements SpellingCorrectionStrategy {

    private final Map<String, Integer> wordCounts;

    public DictionaryOnlyStrategy(Map<String, Integer> wordCounts) {
        this.wordCounts = wordCounts;
    }

    @Override
    public String correct(String word) {
        return wordCounts.containsKey(word.toLowerCase()) ? word : word;
    }
}
