package com.example.searchengine_ver1.backendapi.service;


import com.example.searchengine_ver1.core.model.FileIndex;
import com.example.searchengine_ver1.backendapi.service.observer.PopularFilesTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class RankingService {

    private final PopularFilesTracker popularFilesTracker;

    @Autowired
    public RankingService(PopularFilesTracker popularFilesTracker) {
        this.popularFilesTracker = popularFilesTracker;
    }

    /**
     * Boost score of each file based on how frequently it's been seen in results
     * @param files List of files from the original query
     * @return boosted and sorted list
     */
    public List<FileIndex> rankFiles(List<FileIndex> files) {
        for (FileIndex file : files) {
            double baseScore = file.getScore();
            int frequencyBoost = popularFilesTracker.getScoreBoost(file.getFilePath());

            // Boost logic (scale if needed)
            double finalScore = baseScore + frequencyBoost * 0.5;
            file.setScore(finalScore);
        }

        return files.stream()
                .sorted(Comparator.comparing(FileIndex::getScore).reversed())
                .toList();
    }

    public void updateFileFrequency(List<FileIndex> files) {
        List<String> paths = files.stream()
                .map(FileIndex::getFilePath)
                .toList();

        popularFilesTracker.onSearch(paths); // update frequencies
    }
}
