package com.example.searchengine_ver1.backendapi.service.observer;

import com.example.searchengine_ver1.backendapi.service.subject.RankFilesSubject;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;

@Component
public class PopularFilesTracker implements RankFilesObserver {

    private final Map<String, Integer> fileFrequency = new ConcurrentHashMap<>();
    private final RankFilesSubject rankFilesSubject;

    public PopularFilesTracker(RankFilesSubject rankFilesSubject) {
        this.rankFilesSubject= rankFilesSubject;
        this.rankFilesSubject.registerObserver(this); // custom method for file observers
    }

    @Override
    public void onSearch(List<String> files) {
        for (String file : files) {
            fileFrequency.merge(file, 1, Integer::sum);
        }
    }

    public int getScoreBoost(String filePath) {
        return fileFrequency.getOrDefault(filePath, 0);
    }

    public Map<String, Integer> getTop(int n) {
        return fileFrequency.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(n)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new
                ));
    }
}
