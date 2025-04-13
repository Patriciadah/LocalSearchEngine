package com.example.searchengine_ver1.backendapi.service.observer;

import com.example.searchengine_ver1.backendapi.service.subject.SuggestQuerySubject;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class HistoryTracker implements SuggestQueryObserver {

    private final List<String> history = new LinkedList<>();
    private final SuggestQuerySubject suggestQuerySubject;
    public HistoryTracker(SuggestQuerySubject subject){
        this.suggestQuerySubject=subject;
        this.suggestQuerySubject.registerObserver(this);
    }

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

