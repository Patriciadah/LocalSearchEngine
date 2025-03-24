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
import java.util.Map;
import java.util.stream.Collectors;

import com.example.searchengine_ver1.core.logger.IndexingLogger;

@Service
public class FileIndexer {

    private final FileCrawler fileCrawler;
    private final FileIndexRepository fileIndexRepository;
    private final IndexingLogger indexingLogger;

    @Autowired
    public FileIndexer(FileCrawler fileCrawler, FileIndexRepository fileIndexRepository,IndexingLogger indexingLogger) {
        this.fileCrawler = fileCrawler;
        this.fileIndexRepository = fileIndexRepository;
        this.indexingLogger=indexingLogger;
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
        int totalFiles = files != null ? files.size() : 0;
        int ignoredFiles = 0;
        int indexedFiles = 0;
        List<String> errorLogs = new ArrayList<>();
        /*
        * Maps SQL responses to FileIndex model objects
        * */
        List<FileIndex> fileIndexes = new ArrayList<>();

        assert files != null;
        DebugUtils.writeInFile("Indexed " + files.size() + " files.");
        for (File file : files) {
            try {
                //Metadata metadata = FileUtils.extractMetadata(file);
                // Extract text content using Apache Tika
                String content = FileUtils.extractText(file);
                if(!content.isEmpty())
                    DebugUtils.writeInFile(content);
                else
                {DebugUtils.writeInFile("No content found by apache Tika");
                ignoredFiles++;
                }
                // Extract metadata


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
                indexedFiles++;
            } catch (Exception e) {
                errorLogs.add("Error indexing file: " + file.getAbsolutePath() + " - " + e.getMessage());
                System.err.println("Error indexing file: " + file.getAbsolutePath() + " - " + e.getMessage());
            }
        }

        if (!fileIndexes.isEmpty()) {
            fileIndexRepository.insertAll(fileIndexes); // Bulk insert into MySQL

        }
        DebugUtils.writeInFile("Indexed " + fileIndexes.size() + " files.");
        indexingLogger.generateReport(totalFiles, indexedFiles, ignoredFiles, errorLogs);
    }

    public void updateIndex(String rootDirectory) {
        List<File> files;
        try {
            files = fileCrawler.crawlDirectory(rootDirectory);
        } catch (Exception e) {
            System.err.println("Error during file crawling: " + e.getMessage());
            return;
        }

        // Fetch all indexed files from the database
        List<FileIndex> existingFiles = fileIndexRepository.searchAll();
        Map<String, FileIndex> existingFileMap = existingFiles.stream()
                .collect(Collectors.toMap(FileIndex::getFilePath, file -> file));

        List<FileIndex> newFiles = new ArrayList<>();
        List<FileIndex> updatedFiles = new ArrayList<>();

        for (File file : files) {
            try {
                BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                LocalDateTime lastModified = Instant.ofEpochMilli(attrs.lastModifiedTime().toMillis())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

                FileIndex existingFile = existingFileMap.get(file.getAbsolutePath());

                if (existingFile == null) {
                    // New file - Insert
                    FileIndex newFile = new FileIndex(
                            null,
                            file.getName(),
                            file.getAbsolutePath(),
                            FileUtils.getFileExtension(file),
                            FileUtils.extractText(file),
                            lastModified
                    );
                    newFiles.add(newFile);
                } else if (existingFile.getIndexedAt().isBefore(lastModified)) {
                    // File exists but was modified - Update
                    existingFile.setIndexedAt(lastModified);
                    existingFile.setFileContent(FileUtils.extractText(file));
                    updatedFiles.add(existingFile);
                }
            } catch (Exception e) {
                System.err.println("Error processing file: " + file.getAbsolutePath() + " - " + e.getMessage());
            }
        }

        if (!newFiles.isEmpty()) {
            fileIndexRepository.insertAll(newFiles);
            System.out.println("Inserted " + newFiles.size() + " new files.");
        }
        if (!updatedFiles.isEmpty()) {
            fileIndexRepository.updateAll(updatedFiles);
            System.out.println("Updated " + updatedFiles.size() + " modified files.");
        }
    }

}
