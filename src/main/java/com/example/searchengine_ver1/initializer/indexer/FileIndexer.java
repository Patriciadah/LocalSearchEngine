package com.example.searchengine_ver1.initializer.indexer;

import com.example.searchengine_ver1.core.model.FileIndex;
import com.example.searchengine_ver1.core.repository.FileIndexRepository;
import com.example.searchengine_ver1.core.utils.DebugUtils;
import com.example.searchengine_ver1.initializer.crawler.FileCrawler;
import com.example.searchengine_ver1.core.utils.FileUtils;
import org.apache.tika.metadata.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileIndexer {

    private final FileCrawler fileCrawler;
    private final FileIndexRepository fileIndexRepository;

    @Autowired
    public FileIndexer(FileCrawler fileCrawler, FileIndexRepository fileIndexRepository) {
        this.fileCrawler = fileCrawler;
        this.fileIndexRepository = fileIndexRepository;
    }
    //TODO implement multiple line comment for class and methods
    /**
     * Scans the file system and indexes files into the database.
     * @param rootDirectory The base directory to index.
     */
    public void indexFiles(String rootDirectory) {
        /*
        * Clears the database used for the new root directory
        * */
        fileIndexRepository.clearDatabase();
        DebugUtils.writeInFile("Database is cleared");
        /*
        *  Takes the root directory and crawls recursively to obtain a List of files
        */
        List<File> files=null;
        try{
            files = fileCrawler.crawlDirectory(rootDirectory);
        }
        catch(Exception e){
            // TODO inform for code robustness
            DebugUtils.writeInFile("Something went wong with FileCrawler");
        }

        /*
        * Maps SQL responses to FileIndex model objects
        * */
        List<FileIndex> fileIndexes = new ArrayList<>();

        assert files != null;
        DebugUtils.writeInFile("Indexed " + files.size() + " files.");
        for (File file : files) {
            try {
                // Extract text content using Apache Tika
                String content = FileUtils.extractText(file);
                if(!content.isEmpty())
                    DebugUtils.writeInFile(content);
                else
                    DebugUtils.writeInFile("No content found by apache Tika");
                // Extract metadata
                Metadata metadata = FileUtils.extractMetadata(file);

                // Read file attributes
                BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                LocalDateTime indexedAt = Instant.ofEpochMilli(attrs.creationTime().toMillis())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

                FileIndex fileIndex = new FileIndex(
                        null, // Auto-generated ID
                        file.getName(),
                        file.getAbsolutePath(),
                        FileUtils.getFileExtension(file),
                        content,
                        indexedAt
                );

                fileIndexes.add(fileIndex);
            } catch (Exception e) {
                System.err.println("Error indexing file: " + file.getAbsolutePath() + " - " + e.getMessage());
            }
        }

        if (!fileIndexes.isEmpty()) {
            fileIndexRepository.saveAll(fileIndexes); // Bulk insert into MySQL

        }
        DebugUtils.writeInFile("Indexed " + fileIndexes.size() + " files.");
    }
}
