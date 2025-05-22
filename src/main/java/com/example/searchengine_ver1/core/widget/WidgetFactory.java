package com.example.searchengine_ver1.core.widget;

import com.example.searchengine_ver1.core.model.FileIndex;

import java.util.ArrayList;
import java.util.List;

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
//TODO: see functions
    public List<Widget> getContextWidgets(List<FileIndex> results) {
        List<Widget> widgets = new ArrayList<>();

        long logCount = results.stream().filter(f -> f.getFileType().equalsIgnoreCase("LOG")).count();
        long imageCount = results.stream().filter(f -> f.getFileType().matches("(?i)(jpg|jpeg|png)")).count();

        if (logCount >= 2) {
            widgets.add(new Widget() {
                public String getName() { return "Analyze Logs"; }
                public String getImageUrl() { return "/images/logs.png"; }
            });
        }

        if (imageCount >= 2) {
            widgets.add(new Widget() {
                public String getName() { return "Gallery"; }
                public String getImageUrl() { return "/images/gallery.png"; }
            });
        }

        return widgets;
    }
    }

