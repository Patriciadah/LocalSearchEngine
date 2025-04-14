package com.example.searchengine_ver1;

import com.example.searchengine_ver1.backendapi.service.RankingService;
import com.example.searchengine_ver1.backendapi.service.observer.PopularFilesTracker;
import com.example.searchengine_ver1.core.model.FileIndex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RankingServiceTest {

    private PopularFilesTracker tracker;
    private RankingService rankingService;

    @BeforeEach
    public void setup() {
        tracker = Mockito.mock(PopularFilesTracker.class);
        rankingService = new RankingService(tracker);
    }

    @Test
    public void testBoostIsAppliedCorrectly() {
        FileIndex file1 = new FileIndex(1L, "file1.txt", "/docs/file1.txt", "txt", "some content", LocalDateTime.now(), 5.0);
        FileIndex file2 = new FileIndex(2L, "file2.txt", "/docs/file2.txt", "txt", "some content", LocalDateTime.now(), 3.0);

        Mockito.when(tracker.getScoreBoost("/docs/file1.txt")).thenReturn(2);
        Mockito.when(tracker.getScoreBoost("/docs/file2.txt")).thenReturn(1);

        List<FileIndex> ranked = rankingService.rankFiles(List.of(file1, file2));

        // File1 should now have a higher final score
        assertTrue(ranked.get(0).getFilePath().equals("/docs/file1.txt"));
        assertEquals(6.0, ranked.get(0).getScore());
        assertEquals(3.5, ranked.get(1).getScore());
    }

    @Test
    public void testNoBoostDefaultsToOriginalScore() {
        FileIndex file = new FileIndex(1L, "file.txt", "/docs/file.txt", "txt", "some content", LocalDateTime.now(), 4.0);
        file.setScore(4.0);

        Mockito.when(tracker.getScoreBoost("/docs/file.txt")).thenReturn(0);

        List<FileIndex> ranked = rankingService.rankFiles(List.of(file));

        assertEquals(4.0, ranked.get(0).getScore());
    }
}
