package com.example.searchengine_ver1.core.repository;


import com.example.searchengine_ver1.core.model.FileIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
/**
 *  Implements dto objects using JDBC instead of JPA for better control of queries (lower level)
 *  and efficiency (use of batch inserting)
 *  TODO: see limit of batch and impose error handling
 * */
@Repository
public class FileIndexRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FileIndexRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
/**
 * Batch inserting the file system into database
 * @param files list of files resulting from file crawling
 * */
    public void saveAll(List<FileIndex> files) {
        String sql = "INSERT INTO file_index (file_name, file_path, file_type, file_content, indexed_at) VALUES (?, ?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();
        for (FileIndex file : files) {
            batchArgs.add(new Object[]{
                    file.getFileName(),
                    file.getFilePath(),
                    file.getFileType(),
                    file.getFileContent(),
                    file.getIndexedAt()
            });
        }

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }
    /**
     * Retrieve all match cases from database making use of full-text indexing
     * @param query list of files resulting from file crawling
     * */
    public List<FileIndex> searchFiles(String query) {
        String sql = "SELECT * FROM file_index WHERE MATCH(file_content) AGAINST (? IN NATURAL LANGUAGE MODE)";
        return jdbcTemplate.query(sql, new FileIndexRowMapper(), query);
    }
}

