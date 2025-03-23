package com.example.searchengine_ver1.initializer.service;

import com.example.searchengine_ver1.initializer.indexer.FileIndexer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Start point for initializer service of file data set.
 * Not final implementation
 * */
@Service
public class InitializerService {

    private final FileIndexer fileIndexer;

    @Autowired
    public InitializerService(FileIndexer fileIndexer) {
        this.fileIndexer = fileIndexer;
    }
    @EventListener(ApplicationReadyEvent.class) // Runs after app is fully started
    public void initialize() {
        String rootDirectory = "D:\\Prolog labs"; // Change to your directory
        System.out.println("Starting file indexing...");
        fileIndexer.indexFiles(rootDirectory);
        System.out.println("File indexing completed.");
    }
}