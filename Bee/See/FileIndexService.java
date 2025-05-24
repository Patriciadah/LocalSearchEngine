package com.example.searchengine_ver1.backendapi.service;

import com.example.searchengine_ver1.model.FileIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileIndexService {
    private final FileIndexRepository fileIndexRepository;

    @Autowired
    public FileIndexService(FileIndexRepository fileIndexRepository) {
        this.fileIndexRepository = fileIndexRepository;
    }

    public void indexFiles(List<FileIndex> files) {
        fileIndexRepository.saveAll(files);
    }

    public List<FileIndex> searchFiles(String query) {
        return fileIndexRepository.searchFiles(query);
    }
}
