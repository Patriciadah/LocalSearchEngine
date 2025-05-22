package com.example.searchengine_ver1.backendapi.spellcheck;

import java.util.*;
import java.util.regex.*;

public class NorvigCorrectionStrategy implements SpellingCorrectionStrategy {

    private final Map<String, Integer> wordCounts;
    private final String letters = "abcdefghijklmnopqrstuvwxyz";

    public NorvigCorrectionStrategy(Map<String, Integer> wordCounts) {
        this.wordCounts = wordCounts;
    }

    private double probability(String word) {
        return wordCounts.getOrDefault(word, 0) /
                (double) wordCounts.values().stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public String correct(String word) {
        return candidates(word).stream()
                .max(Comparator.comparingDouble(this::probability))
                .orElse(word);
    }

    private Set<String> candidates(String word) {
        Set<String> known = known(Set.of(word));
        if (!known.isEmpty()) return known;

        Set<String> edits1 = known(edits1(word));
        if (!edits1.isEmpty()) return edits1;

        Set<String> edits2 = known(edits2(word));
        if (!edits2.isEmpty()) return edits2;

        return Set.of(word);
    }

    private Set<String> known(Set<String> words) {
        Set<String> result = new HashSet<>();
        for (String w : words) {
            if (wordCounts.containsKey(w)) result.add(w);
        }
        return result;
    }

    private Set<String> edits1(String word) {
        Set<String> edits = new HashSet<>();
        for (int i = 0; i <= word.length(); i++) {
            String L = word.substring(0, i);
            String R = word.substring(i);
            if (!R.isEmpty()) edits.add(L + R.substring(1));
            if (R.length() > 1) edits.add(L + R.charAt(1) + R.charAt(0) + R.substring(2));
            for (char c : letters.toCharArray()) {
                if (!R.isEmpty()) edits.add(L + c + R.substring(1));
                edits.add(L + c + R);
            }
        }
        return edits;
    }

    private Set<String> edits2(String word) {
        Set<String> result = new HashSet<>();
        for (String e1 : edits1(word)) {
            result.addAll(edits1(e1));
        }
        return result;
    }
}
