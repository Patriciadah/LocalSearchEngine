package com.example.searchengine_ver1.backendapi.service;

import com.example.searchengine_ver1.core.model.FileIndex;
import com.example.searchengine_ver1.core.repository.FileIndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

@Service
public class SearchService implements CommandLineRunner {
    FileIndexRepository fileIndexRepository;
    @Autowired
    public SearchService(FileIndexRepository fileIndexRepository){
        this.fileIndexRepository=fileIndexRepository;
    }
    public void searchAndDisplayFiles(String query) {
        List<FileIndex> results = fileIndexRepository.searchFiles(query);

        if (results.isEmpty()) {
            System.out.println("No matches found for: " + query);
            return;
        }

        System.out.println("Search Results:");
        for (FileIndex file : results) {
            System.out.println("\nFile: " + file.getFileName() + " | Path: " + file.getFilePath());

            // Extract first 3 lines from file content
            String[] lines = file.getFileContent().split("\n");
            for (int i = 0; i < Math.min(3, lines.length); i++) {
                System.out.println("  " + lines[i]);
            }
        }
    }
    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("\nEnter search query (or type 'exit' to quit): ");
            String userQuery = scanner.nextLine();

            if ("exit".equalsIgnoreCase(userQuery)) {
                System.out.println("Exiting search...");
                break;
            }

            searchAndDisplayFiles(userQuery);
        }
        scanner.close();
    }
}
