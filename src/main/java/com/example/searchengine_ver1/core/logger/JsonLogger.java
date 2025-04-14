package com.example.searchengine_ver1.core.logger;

import com.example.searchengine_ver1.core.utils.DebugUtils;
import com.example.searchengine_ver1.core.utils.LoggerUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonLogger implements IndexingLogger {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void generateReport(int total, int indexed, int ignored, List<String> errors) {
        Map<String, Object> report = new HashMap<>();
        report.put("totalFiles", total);
        report.put("indexedFiles", indexed);
        report.put("ignoredFiles", ignored);
        report.put("errors", errors);

        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(report);
            LoggerUtils.writeInFile(json);
        } catch (Exception e) {
            LoggerUtils.writeInFile("Error generating JSON report: " + e.getMessage());
        }
    }
}

