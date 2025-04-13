package com.example.searchengine_ver1.core.logger;

import com.example.searchengine_ver1.core.utils.DebugUtils;

import java.util.List;

public class PlainTextLogger implements IndexingLogger {
    @Override
    public void generateReport(int total, int indexed, int ignored, List<String> errors) {
        DebugUtils.writeInFile("---- Indexing Report ----");
        DebugUtils.writeInFile("Total files found: " + total);
        DebugUtils.writeInFile("Files indexed: " + indexed);
        DebugUtils.writeInFile("Files ignored: " + ignored);
        if (!errors.isEmpty()) {
            DebugUtils.writeInFile("Errors:");
            errors.forEach(DebugUtils::writeInFile);
        }
    }
}

