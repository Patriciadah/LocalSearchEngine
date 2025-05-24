package com.example.searchengine_ver1.backendapi.service;

import com.example.searchengine_ver1.backendapi.service.observer.HistoryTracker;
import com.example.searchengine_ver1.backendapi.service.observer.PopularQueryTracker;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SuggestionService {

    private final HistoryTracker historyTracker;
    private final PopularQueryTracker popularQueryTracker;

    public SuggestionService(HistoryTracker historyTracker, PopularQueryTracker popularQueryTracker) {
        this.historyTracker = historyTracker;
        this.popularQueryTracker = popularQueryTracker;
    }

    public List<String> suggestPopular() {

        return popularQueryTracker.getTop(5).keySet().stream()
                .limit(5)
                .toList();
    }
    public List<String> suggestMostRecent() {

        return historyTracker.getRecentQueries().stream()
                .limit(5)
                .toList();
    }
    public Map<String, List<String>> getStructuredSuggestions() {
        Map<String, List<String>> result = new HashMap<>();
        result.put("popular", suggestPopular());
        result.put("recent", suggestMostRecent());
        return result;
    }

    public String suggest(){
        List<String> popularSuggestions = suggestPopular();
        List<String> recentSuggestions = suggestMostRecent();

       String popularString = popularSuggestions.isEmpty()
                ? ""
                : "Popular suggestions: " + String.join(", ", popularSuggestions);

        String recentString = recentSuggestions.isEmpty()
                ? ""
                : "Recent suggestions: " + String.join(", ", recentSuggestions);

        if (popularString.isEmpty() && recentString.isEmpty()) {
            return "No suggestions found yet.";
        } else if (popularString.isEmpty()) {
            return recentString;
        } else if (recentString.isEmpty()) {
            return popularString;
        } else {
            return popularString + "\n" + recentString;
        }
    }
}
