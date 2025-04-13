package com.example.searchengine_ver1.core.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;

public class ScoreUtils {

    private static final Set<String> PREFERRED_EXTENSIONS = Set.of("java", "txt", "pdf", "word");
    private static final double RECENT_FILE_BOOST = 2.0;
    private static final double MAX_SCORE = 10.0; // Optional: clamp total score

    public static double computeScore(File file, String content, LocalDateTime lastModified) {
        double score = 0;

        // Extension preference
        String extension = FileUtils.getFileExtension(file);
        if (PREFERRED_EXTENSIONS.contains(extension)) score += 2;

        // Path depth — deeper = less important
        int pathDepth = file.toPath().getNameCount();
        score += 1.0 / (double) pathDepth;

        // File size preference — prefer midsize files
        long fileSize = file.length();
        if (fileSize > 100 && fileSize < 5000) score += 1;

        // Recency scoring (within this month)
        LocalDateTime now = LocalDateTime.now();
        if (lastModified.getMonth().equals(now.getMonth()) &&
                lastModified.getYear() == now.getYear()) {
            score += RECENT_FILE_BOOST;
        }

        // Optional: clamp score
        return Math.min(score, MAX_SCORE);
    }

}
