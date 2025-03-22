package com.example.searchengine_ver1.initializer.crawler;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileCrawler {
    public List<File> crawlDirectory(String rootDir) throws Exception {
        return Files.walk(Paths.get(rootDir))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());
    }
}