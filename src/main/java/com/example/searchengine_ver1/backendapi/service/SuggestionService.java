package com.example.searchengine_ver1.backendapi.service;

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

    public List<String> suggest(String partialQuery) {
        String lower = partialQuery.toLowerCase();
        return popularQueryTracker.getTop(20).keySet().stream()
                .filter(q -> q.startsWith(lower))
                .limit(5)
                .toList();
    }
}
