package com.example.searchengine_ver1.backendapi.proxy;

import com.example.searchengine_ver1.backendapi.proxy.SearchServiceInterface;
import com.example.searchengine_ver1.backendapi.service.RankingService;
import com.example.searchengine_ver1.backendapi.service.SuggestionService;
import com.example.searchengine_ver1.backendapi.service.observer.HistoryTracker;
import com.example.searchengine_ver1.backendapi.service.observer.PopularFilesTracker;
import com.example.searchengine_ver1.backendapi.service.observer.PopularQueryTracker;
import com.example.searchengine_ver1.backendapi.service.subject.RankFilesSubject;
import com.example.searchengine_ver1.backendapi.service.subject.SuggestQuerySubject;
import com.example.searchengine_ver1.core.model.FileIndex;
import com.example.searchengine_ver1.core.repository.FileIndexRepository;
import com.example.searchengine_ver1.core.utils.QueryParserUtils;
import com.example.searchengine_ver1.exception.ContentNotPresentException;
import com.example.searchengine_ver1.initializer.indexer.FileIndexer;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.OptionalInt;

@Service

public class RealSearchService implements SearchServiceInterface {
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
    public RealSearchService(FileIndexRepository fileIndexRepository, SuggestQuerySubject suggestQuerySubject, HistoryTracker historyTracker, PopularQueryTracker popularQueryTracker, PopularFilesTracker popularFilesTracker, RankFilesSubject rankFilesSubject, FileIndexer fileIndexer){
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
    public List<FileIndex> search(String query) throws ContentNotPresentException {
        Map<String, List<String>> parsed = QueryParserUtils.parseQuery(query);
        List<FileIndex> results;

        // Extracts content, path, fileType
        List<String> contentTerms = parsed.getOrDefault("content", List.of());
        List<String> pathTerms = parsed.getOrDefault("path", List.of());
        List<String> fileTypes = parsed.getOrDefault("fileType", List.of());

        // Join content terms together
        String contentString = String.join(" ", contentTerms);

            // Search from DB using SQL filters - content, path, fileTypes
            //                                      -> Main filters for indexing
            results = fileIndexRepository.searchWithFilters(contentTerms, pathTerms, fileTypes);

            // Apply score filter
            OptionalDouble minScore = QueryParserUtils.extractMinScore(parsed);
            if (minScore.isPresent()) {
                results = results.stream()
                        .filter(f -> f.getScore() != null && f.getScore() > minScore.getAsDouble())
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
                 return null;
            }

            suggestQuerySubject.useQuery(contentString.isEmpty() ? query : contentString);
            rankFilesSubject.notifyObservers(results.stream().map(FileIndex::getFilePath).toList());
            return results;

    }
}
