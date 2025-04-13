package com.example.searchengine_ver1.backendapi.service;

import com.example.searchengine_ver1.backendapi.service.observer.HistoryTracker;
import com.example.searchengine_ver1.backendapi.service.observer.PopularQueryTracker;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuggestionService {

    private final HistoryTracker historyTracker;
    private final PopularQueryTracker popularQueryTracker;

    public SuggestionService(HistoryTracker historyTracker, PopularQueryTracker popularQueryTracker) {
        this.historyTracker = historyTracker;
        this.popularQueryTracker = popularQueryTracker;
    }

    public List<String> suggestPopular(String partialQuery) {
        String lower = partialQuery.toLowerCase();
        return popularQueryTracker.getTop(5).keySet().stream()
                .filter(q -> q.startsWith(lower))
                .limit(5)
                .toList();
    }
    public List<String> suggestMostRecent(String partialQuery) {
        String lower = partialQuery.toLowerCase();
        return historyTracker.getRecentQueries().stream()
                .filter(q -> q.startsWith(lower))
                .limit(5)
                .toList();
    }
    public String suggest(String partialQuery){
        List<String> popularSuggestions = suggestPopular(partialQuery);
        List<String> recentSuggestions = suggestMostRecent(partialQuery);

        String popularString = popularSuggestions.isEmpty()
                ? ""
                : "Popular suggestions: " + String.join(", ", popularSuggestions);

        String recentString = recentSuggestions.isEmpty()
                ? ""
                : "Recent suggestions: " + String.join(", ", recentSuggestions);

        if (popularString.isEmpty() && recentString.isEmpty()) {
            return "No suggestions found for '" + partialQuery + "'.";
        } else if (popularString.isEmpty()) {
            return recentString;
        } else if (recentString.isEmpty()) {
            return popularString;
        } else {
            return popularString + " | " + recentString;
        }
    }
}
