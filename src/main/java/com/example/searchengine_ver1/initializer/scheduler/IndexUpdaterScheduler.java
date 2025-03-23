package com.example.searchengine_ver1.initializer.scheduler;

import com.example.searchengine_ver1.initializer.indexer.FileIndexer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class IndexUpdaterScheduler {

    private final FileIndexer fileIndexer;
    private final String rootDirectory = "D:\\Prolog labs";

    @Autowired
    public IndexUpdaterScheduler(FileIndexer fileIndexer) {
        this.fileIndexer = fileIndexer;
    }

    @Scheduled(fixedRate = 3600000) // Runs every hour
    public void scheduledIndexUpdate() {
        System.out.println("Running scheduled index update...");
        fileIndexer.updateIndex(rootDirectory);
    }
}
