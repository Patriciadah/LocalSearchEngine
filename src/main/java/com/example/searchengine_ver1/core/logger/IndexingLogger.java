package com.example.searchengine_ver1.core.logger;

import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
@Component
public class IndexingLogger {
    public void generateReport(int totalFiles, int indexedFiles, int ignoredFiles, List<String> errors) {
        try (FileWriter writer = new FileWriter("index_report.txt")) {
            writer.write("File Indexing Report\n");
            writer.write("------------------------------\n");
            writer.write("Total Files Scanned: " + totalFiles + "\n");
            writer.write("Files Indexed: " + indexedFiles + "\n");
            writer.write("Files Ignored: " + ignoredFiles + "\n");
            writer.write("Errors: " + errors.size() + "\n\n");

            for (String error : errors) {
                writer.write(error + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing report: " + e.getMessage());
        }
    }
}