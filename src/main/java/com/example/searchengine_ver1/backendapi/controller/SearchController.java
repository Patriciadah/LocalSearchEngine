package com.example.searchengine_ver1.backendapi.controller;

import com.example.searchengine_ver1.backendapi.service.SearchService;
import com.example.searchengine_ver1.core.model.FileIndex;
import com.example.searchengine_ver1.core.model.SearchResponse;
import com.example.searchengine_ver1.exception.ContentNotPresentException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private SearchService searchService;


    @GetMapping
    public ResponseEntity<SearchResponse> search(@RequestParam String query) {
        List<FileIndex> results;

        try {
            results = searchService.search(query);
        } catch (ContentNotPresentException e) {
            return ResponseEntity.badRequest().body(new SearchResponse("Please introduce content."));
        }

        if (results == null || results.isEmpty()) {
            return ResponseEntity.ok(new SearchResponse("No results found for: " + query));
        }



        SearchResponse response = new SearchResponse();
        response.setResults(results);
        //response.setSpecialWidgets(specialWidgets);
        //response.setContextWidgets(contextWidgets);
        //response.setMetadataSummary(metadataAnalyzer.analyze(results));
        response.setMessage("Success");

        return ResponseEntity.ok(response);
    }
}
