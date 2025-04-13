package com.example.searchengine_ver1.core.logger;

import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
@Component
public interface IndexingLogger {
    public void generateReport(int totalFiles, int indexedFiles, int ignoredFiles, List<String> errors);
}