package com.example.searchengine_ver1.backendapi.service;

import com.example.searchengine_ver1.backendapi.service.observer.HistoryTracker;
import com.example.searchengine_ver1.backendapi.service.observer.PopularFilesTracker;
import com.example.searchengine_ver1.backendapi.service.observer.PopularQueryTracker;
import com.example.searchengine_ver1.backendapi.service.subject.RankFilesSubject;
import com.example.searchengine_ver1.backendapi.service.subject.SuggestQuerySubject;
import com.example.searchengine_ver1.core.model.FileIndex;
import com.example.searchengine_ver1.core.repository.FileIndexRepository;
import com.example.searchengine_ver1.core.utils.QueryParserUtils;
import com.example.searchengine_ver1.initializer.indexer.FileIndexer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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
    FileIndexer fileIndexer;

    @Autowired
    public SearchService(FileIndexRepository fileIndexRepository,SuggestQuerySubject suggestQuerySubject,HistoryTracker historyTracker,PopularQueryTracker popularQueryTracker,PopularFilesTracker popularFilesTracker,RankFilesSubject rankFilesSubject,FileIndexer fileIndexer){
        this.fileIndexer=fileIndexer;
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
        Map<String, List<String>> parsed = QueryParserUtils.parseQuery(query);
        List<FileIndex> results = new ArrayList<>();

        List<String> contentTerms = parsed.getOrDefault("content", List.of());
        List<String> pathTerms = parsed.getOrDefault("path", List.of());
        List<String> fileTypes = parsed.getOrDefault("fileType", List.of());

        String contentString = String.join(" ", contentTerms);

        // Re-index if path filter is specified
//        if (!pathTerms.isEmpty()) {
//            for (String path : pathTerms) {
//                fileIndexer.indexFiles(path);
//            }
//        }

        // Search from DB using SQL filters
        results = fileIndexRepository.searchWithFilters(contentTerms, pathTerms, fileTypes);

        // Apply score filter
        OptionalDouble minScore = QueryParserUtils.extractMinScore(parsed);
        if (minScore.isPresent()) {
            results = results.stream()
                    .filter(f -> f.getScore() != null && f.getScore() >= minScore.getAsDouble())
                    .toList();
        }

        // Apply date filter
        OptionalInt modifiedSince = QueryParserUtils.extractModifiedSinceDays(parsed);
        if (modifiedSince.isPresent()) {
            LocalDateTime threshold = LocalDateTime.now().minusDays(modifiedSince.getAsInt());
            results = results.stream()
                    .filter(f -> f.getIndexedAt() != null && f.getIndexedAt().isAfter(threshold))
                    .toList();
        }

        // Ranking
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

        suggestQuerySubject.useQuery(contentString.isEmpty() ? query : contentString);
        rankFilesSubject.notifyObservers(results.stream().map(FileIndex::getFilePath).toList());
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
