package com.example.searchengine_ver1.core.model;

import com.example.searchengine_ver1.core.widget.Widget;
import com.example.searchengine_ver1.core.model.FileIndex;

import java.util.List;
import java.util.Map;

public class SearchResponse {
    private String message;
    private List<FileIndex> results;
    private List<Widget> specialWidgets;
    private List<Widget> contextWidgets;
    private Map<String, Integer> metadataSummary;

    public SearchResponse() {}

    public SearchResponse(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<FileIndex> getResults() { return results; }
    public void setResults(List<FileIndex> results) { this.results = results; }

    public List<Widget> getSpecialWidgets() { return specialWidgets; }
    public void setSpecialWidgets(List<Widget> specialWidgets) { this.specialWidgets = specialWidgets; }

    public List<Widget> getContextWidgets() { return contextWidgets; }
    public void setContextWidgets(List<Widget> contextWidgets) { this.contextWidgets = contextWidgets; }

    public Map<String, Integer> getMetadataSummary() { return metadataSummary; }
    public void setMetadataSummary(Map<String, Integer> metadataSummary) { this.metadataSummary = metadataSummary; }
}

