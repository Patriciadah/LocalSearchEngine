package com.example.searchengine_ver1.backendapi.service;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HistoryTracker implements SearchObserver {

    private final List<String> history = new LinkedList<>();

    @Override
    public void onSearch(String query) {
        if (query != null && !query.trim().isEmpty()) {
            history.add(0, query); // Add to the beginning (latest first)
            if (history.size() > 10) {
                history.remove(history.size() - 1); // Keep last 10 searches
            }
        }
    }
    public List<String> getRecentQueries() {
        return List.copyOf(history);
    }
}

