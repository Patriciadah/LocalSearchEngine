package com.example.searchengine_ver1.core.widget;

import com.example.searchengine_ver1.core.model.FileIndex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WidgetFactory {

    public static Widget createWidget(String keyword) {
        return switch (keyword.toLowerCase()) {
            case "calculator" -> new CalculatorWidget();
            case "saturn" -> new SaturnWidget();
            default -> null;
        };
    }

    public List<Widget> getSpecialWidgets(String query) {
        List<Widget> widgets = new ArrayList<>();
        if (query.toLowerCase().contains("calculator")) widgets.add(createWidget("calculator"));
        if (query.toLowerCase().contains("saturn")) widgets.add(createWidget("saturn"));
        return widgets;
    }

    public Map<String, Boolean> getContextWidgetFlags(List<FileIndex> results) {
        // Calculate log file count
        long logCount = results.stream()
                .filter(f -> f.getFileType().matches("(?i)(Text)") && f.getFileName().toLowerCase().endsWith(".log"))
                .count();

        // Calculate image file count using the regex for file type
        long imageCount = results.stream()
                .filter(f -> f.getFileType() != null && f.getFileType().matches("(?i)(Image)"))
                .count();

        // Initialize the map with flags set to false
        Map<String, Boolean> contextFlags = new HashMap<>();
        contextFlags.put("logFlag", false);
        contextFlags.put("imageFlag", false);

        // Check conditions and update flags
        if (logCount >= 2) {
            contextFlags.put("logFlag", true);
        }

        if (imageCount >= 2) {
            contextFlags.put("imageFlag", true);
        }

        return contextFlags;
    }

}