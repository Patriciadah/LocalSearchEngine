package com.example.searchengine_ver1.backendapi.controller;

import com.example.searchengine_ver1.backendapi.proxy.SearchServiceInterface;
import com.example.searchengine_ver1.backendapi.service.RealSearchService;
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

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private RealSearchService realSearchService;
    private final WidgetFactory widgetFactory = new WidgetFactory();
    private final MetadataAnalyzer metadataAnalyzer = new MetadataAnalyzer();

    @Autowired
    @Qualifier("cachedSearchProxy")
    private SearchServiceInterface searchService;
    @GetMapping
    public ResponseEntity<SearchResponse> search(@RequestParam String query) {
        List<FileIndex> results;

        try {
            results = realSearchService.search(query);
        } catch (ContentNotPresentException e) {
            return ResponseEntity.badRequest().body(new SearchResponse("Please introduce content."));
        }

        if (results == null || results.isEmpty()) {
            return ResponseEntity.ok(new SearchResponse("No results found for: " + query));
        }

        List<Widget> specialWidgets = widgetFactory.getSpecialWidgets(query);
        List<Widget> contextWidgets = widgetFactory.getContextWidgets(results);

        SearchResponse response = new SearchResponse();
        response.setResults(results);
        response.setSpecialWidgets(specialWidgets);
        response.setContextWidgets(contextWidgets);
        response.setMetadataSummary(metadataAnalyzer.analyze(results));
        response.setMessage("Success");

        return ResponseEntity.ok(response);
    }
}
