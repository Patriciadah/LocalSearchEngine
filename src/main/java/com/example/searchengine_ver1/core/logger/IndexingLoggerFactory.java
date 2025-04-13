package com.example.searchengine_ver1.core.logger;

public class IndexingLoggerFactory {
    public static IndexingLogger createLogger(String format) {
        return switch (format.toUpperCase()) {
            case "JSON" -> new JsonLogger();
            default -> new PlainTextLogger();
        };
    }
}


