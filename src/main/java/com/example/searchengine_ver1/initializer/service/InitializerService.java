package com.example.searchengine_ver1.initializer.service;

import com.example.searchengine_ver1.core.utils.DebugUtils;
import com.example.searchengine_ver1.initializer.indexer.FileIndexer;
import org.springframework.beans.factory.annotation.Autowired;
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

    // Trigger manually when needed (e.g., from an API endpoint)
    public void initialize(String rootDirectory) {
        DebugUtils.clearContent();
        DebugUtils.writeInFile("Starting file indexing...");
        fileIndexer.indexFiles(rootDirectory);
        DebugUtils.writeInFile("File indexing completed.");

    }
}