package com.example.searchengine_ver1.initializer.crawler;
import com.example.searchengine_ver1.core.utils.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
/**
 *  Crawls the file system and returns a list of type File(non-directory).
 *  Taken as a Component Bean
 * */
@Component
public class FileCrawler {
    private static final Set<String> IGNORED_EXTENSIONS = Set.of("exe", "tmp", "log", "bin","md","tlog");
    /**
     *  @param rootDir the root directory to start crawler
     *  @return list of files
     * */
    public List<File> crawlDirectory(String rootDir) throws Exception {
        return Files.walk(Paths.get(rootDir))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .filter(this::isValidFileIgnoreRules)
                .collect(Collectors.toList());
    }
    /**
     * Checks if a file should be indexed (ignores certain file extensions).
     * For future use when ignoring rules are required
     * */
    private boolean isValidFileIgnoreRules(File file) {
        String extension = FileUtils.getFileExtension(file);
        System.out.println("Extension for:" + file.getPath()+" "+ extension);
        return !IGNORED_EXTENSIONS.contains(extension);
    }
    private boolean isValidFileTxT(File file) {
        String extension = FileUtils.getFileExtension(file);
        return extension.equals("plain");
    }
}