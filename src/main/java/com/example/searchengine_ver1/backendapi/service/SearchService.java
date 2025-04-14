package com.example.searchengine_ver1.backendapi.service;

import com.example.searchengine_ver1.backendapi.service.observer.HistoryTracker;
import com.example.searchengine_ver1.backendapi.service.observer.PopularFilesTracker;
import com.example.searchengine_ver1.backendapi.service.observer.PopularQueryTracker;
import com.example.searchengine_ver1.backendapi.service.subject.RankFilesSubject;
import com.example.searchengine_ver1.backendapi.service.subject.SuggestQuerySubject;
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
    SuggestQuerySubject suggestQuerySubject;
    RankFilesSubject rankFilesSubject;
    HistoryTracker historyTracker;
    PopularQueryTracker popularQueryTracker;
    SuggestionService suggestionService;
    PopularFilesTracker popularFilesTracker;
    RankingService rankingService;

    @Autowired
    public SearchService(FileIndexRepository fileIndexRepository,SuggestQuerySubject suggestQuerySubject,HistoryTracker historyTracker,PopularQueryTracker popularQueryTracker,PopularFilesTracker popularFilesTracker,RankFilesSubject rankFilesSubject){
        this.fileIndexRepository=fileIndexRepository;
        this.suggestQuerySubject=suggestQuerySubject;
        this.popularQueryTracker=popularQueryTracker;
        this.historyTracker=historyTracker;
        this.suggestionService= new SuggestionService(historyTracker,popularQueryTracker);
        this.rankFilesSubject=rankFilesSubject;
        this.popularFilesTracker=popularFilesTracker;
        this.rankingService= new RankingService(popularFilesTracker);
        configureRankingSubject();

    }

    private void configureRankingSubject(){
        rankFilesSubject.registerObserver(popularFilesTracker);
    }
    public void searchAndDisplayFiles(String query) {
        List<FileIndex> results = fileIndexRepository.searchFiles(query);
        results = rankingService.rankFiles(results);

        if (results.isEmpty()) {
            System.out.println("No matches found for: " + query);
            return;
        }

        System.out.println("Search Results:");
        for (FileIndex file : results) {
            System.out.println("\nFile: " + file.getFileName() + " | Path: " + file.getFilePath());

            String[] lines = file.getFileContent().split("\n");
            for (int i = 0; i < Math.min(3, lines.length); i++) {
                System.out.println("  " + lines[i]);
            }
        }

        // Notify query-based observers
        suggestQuerySubject.useQuery(query);

        // Notify file observers with file paths
        rankFilesSubject.notifyObservers(
                results.stream().map(FileIndex::getFilePath).toList()
        );
    }


    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {

            System.out.print("\nEnter search query (or type 'exit' to quit): ");


            String suggestion=suggestionService.suggest();
            System.out.println(suggestion);
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
