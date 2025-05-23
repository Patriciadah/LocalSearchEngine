package com.example.searchengine_ver1.backendapi.service.observer;

import com.example.searchengine_ver1.backendapi.service.subject.SuggestQuerySubject;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;

@Component
public class PopularQueryTracker implements SuggestQueryObserver {

    private final Map<String, Integer> queryFrequency = new ConcurrentHashMap<>();
    private final SuggestQuerySubject suggestQuerySubject;
    public PopularQueryTracker(SuggestQuerySubject s){
        this.suggestQuerySubject=s;
        this.suggestQuerySubject.registerObserver(this);
    }
    @Override
    public void onSearch(String query) {
        queryFrequency.merge(query.toLowerCase(), 1, Integer::sum);
    }

    public Map<String, Integer> getTop(int n) {
        return queryFrequency.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(n)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new
                ));
    }

    public int getScoreBoost(String query) {
        return queryFrequency.getOrDefault(query.toLowerCase(), 0);
    }
}

