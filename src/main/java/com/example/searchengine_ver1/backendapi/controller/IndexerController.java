package com.example.searchengine_ver1.backendapi.controller;

import com.example.searchengine_ver1.initializer.service.InitializerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import static com.example.searchengine_ver1.core.utils.DebugUtils.writeInFile;

@RestController
@RequestMapping("/indexer")
public class IndexerController {

    private final InitializerService initializerService;

    @Autowired
    public IndexerController(InitializerService initializerService) {
        this.initializerService = initializerService;
    }

    @PostMapping("/initialize")
    public ResponseEntity<String> reindex(@RequestParam String directory) {
        initializerService.initialize(directory);
        return ResponseEntity.ok("Reindexing started for directory: " + directory);
    }

    }
