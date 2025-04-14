package com.example.searchengine_ver1.core.utils.trimmer;

import com.example.searchengine_ver1.core.model.FileIndex;

import java.util.List;
import java.util.stream.Collectors;

public class FileIndexTrimmer {

    public static List<FileIndex> trimFileIndexContents(List<FileIndex> fileIndices) {
        return fileIndices.stream()
                .map(FileIndexTrimmer::trimFileIndexContent)
                .collect(Collectors.toList());
    }

    public static FileIndex trimFileIndexContent(FileIndex fileIndex) {
        if (fileIndex != null) {
            String content = fileIndex.getFileContent();
            if (content != null) {
                String trimmedContent = StringTrimmer.trimToKB(content,50);
                fileIndex.setFileContent(trimmedContent);
            }
        }
        return fileIndex;
    }
}
