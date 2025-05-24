package com.example.searchengine_ver1.backendapi.controller;

import com.example.searchengine_ver1.backendapi.proxy.SearchServiceInterface;
import com.example.searchengine_ver1.core.analysis.MetadataAnalyzer;
import com.example.searchengine_ver1.core.model.FileIndex;
import com.example.searchengine_ver1.core.model.SearchResponse;
import com.example.searchengine_ver1.exception.ContentNotPresentException;
import com.example.searchengine_ver1.core.widget.WidgetFactory;
import com.example.searchengine_ver1.core.widget.Widget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    @Qualifier("cachedSearchProxy")
    private SearchServiceInterface searchService;

    private final WidgetFactory widgetFactory = new WidgetFactory();
    private final MetadataAnalyzer metadataAnalyzer = new MetadataAnalyzer();

    @GetMapping
    public ResponseEntity<SearchResponse> search(@RequestParam String query) {
        SearchResponse response = new SearchResponse();

        List<FileIndex> results;
        try {
            results = searchService.search(query);
        } catch (ContentNotPresentException e) {
            response.setMessage("Please introduce content.");
            return ResponseEntity.badRequest().body(response);
        }

        // Always include special widgets (image-based)
        List<Widget> specialWidgets = widgetFactory.getSpecialWidgets(query);
        response.setSpecialWidgets(specialWidgets);

        if (results == null || results.isEmpty()) {
            String justWord = query.contains(":") ? query.substring(query.indexOf(':') + 1) : query;
            response.setMessage("No results found for: " + justWord);
            return ResponseEntity.ok(response);
        }

        // Set results and context-aware widget flags
        response.setResults(results);
        Map<String, Boolean> contextFlags = widgetFactory.getContextWidgetFlags(results);
        response.setShowAnalyzeLogs(contextFlags.getOrDefault("logFlag", false));
        response.setShowGallery(contextFlags.getOrDefault("imageFlag", false));

        // Set metadata and message
        response.setMetadataSummary(metadataAnalyzer.analyze(results));
        response.setMessage("Success");

        return ResponseEntity.ok(response);
    }
}
