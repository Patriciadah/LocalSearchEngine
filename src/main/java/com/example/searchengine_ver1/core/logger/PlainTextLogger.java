package com.example.searchengine_ver1.core.logger;

import com.example.searchengine_ver1.core.utils.DebugUtils;
import com.example.searchengine_ver1.core.utils.LoggerUtils;

import java.util.List;

public class PlainTextLogger implements IndexingLogger {
    @Override
    public void generateReport(int total, int indexed, int ignored, List<String> errors) {
        LoggerUtils.writeInFile("---- Indexing Report ----");
        LoggerUtils.writeInFile("Total files found: " + total);
        LoggerUtils.writeInFile("Files indexed: " + indexed);
        LoggerUtils.writeInFile("Files ignored: " + ignored);
        if (!errors.isEmpty()) {
            LoggerUtils.writeInFile("Errors:");
            errors.forEach(DebugUtils::writeInFile);
        }
    }
}

