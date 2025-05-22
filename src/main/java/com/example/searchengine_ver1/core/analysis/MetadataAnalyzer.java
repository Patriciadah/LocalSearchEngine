package com.example.searchengine_ver1.core.analysis;

import com.example.searchengine_ver1.core.model.FileIndex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetadataAnalyzer {

    public Map<String, Integer> analyze(List<FileIndex> results) {
        Map<String, Integer> summary = new HashMap<>();

        for (FileIndex f : results) {
            // File Type
            summary.merge(f.getFileType(), 1, Integer::sum);

            // Year
            if (f.getIndexedAt() != null) {
                String year = String.valueOf(f.getIndexedAt().getYear());
                summary.merge(year, 1, Integer::sum);
            }
        }

        return summary;
    }
}
