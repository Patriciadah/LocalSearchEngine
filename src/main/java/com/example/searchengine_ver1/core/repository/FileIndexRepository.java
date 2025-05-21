package com.example.searchengine_ver1.core.repository;


import com.example.searchengine_ver1.core.model.FileIndex;
import com.example.searchengine_ver1.core.utils.DebugUtils;
import com.example.searchengine_ver1.exception.ContentNotPresentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import static com.example.searchengine_ver1.core.utils.DebugUtils.writeInFile;
import static com.example.searchengine_ver1.core.utils.trimmer.FileIndexTrimmer.trimFileIndexContents;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public void insertAll(List<FileIndex> files) {
        files=trimFileIndexContents(files);
        String sql = "INSERT INTO file_index (file_name, file_path, file_type, file_content, indexed_at,score) VALUES (?, ?, ?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();
        for (FileIndex file : files) {
            batchArgs.add(new Object[]{
                    file.getFileName(),
                    file.getFilePath(),
                    file.getFileType(),
                    file.getFileContent(),
                    file.getIndexedAt(),
                    file.getScore()
            });
        }

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    public void updateAll(List<FileIndex> files) {
        String sql = "UPDATE file_index " +
                "SET file_name = ?, file_type = ?, file_content = ?, indexed_at = ?, score= ?" +
                "WHERE file_path = ?";

        List<Object[]> batchArgs = new ArrayList<>();
        for (FileIndex file : files) {
            batchArgs.add(new Object[]{
                    file.getFileName(),
                    file.getFileType(),
                    file.getFileContent(),
                    file.getIndexedAt(),
                    file.getScore(),
                    file.getFilePath() // WHERE condition
            });
        }

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    /**
     * Retrieve all match cases from database making use of full-text indexing
     * @param query list of files resulting from file crawling
     * */
    public List<FileIndex> searchFiles(String query) {
        String sql = "SELECT * FROM file_index WHERE MATCH(file_name,file_content) AGAINST (? IN NATURAL LANGUAGE MODE)";
        return jdbcTemplate.query(sql, new FileIndexRowMapper(), query);
    }
    public List<FileIndex> searchAll() {
        String sql = "SELECT * FROM file_index";
        return jdbcTemplate.query(sql, new FileIndexRowMapper());
    }
    /**
     * Reinitialize database to store the new system of files
     * */
    public void clearDatabase() {
        DebugUtils.writeInFile("Resetting database...");
        clearTable();
        resetAutoIncrement();
    }
    private void clearTable(){
        jdbcTemplate.update("DELETE FROM file_index");
    }

    private void resetAutoIncrement() {
        try {
            jdbcTemplate.execute("ALTER TABLE file_index AUTO_INCREMENT = 1");
            DebugUtils.writeInFile("Auto-increment reset.");
        } catch (Exception e) {
            System.err.println("Error resetting auto-increment: " + e.getMessage());
        }
    }
    public List<FileIndex> searchWithFilters(List<String> contentTerms, List<String> pathTerms, List<String> fileTypes) throws ContentNotPresentException {
        StringBuilder sql = new StringBuilder("SELECT * FROM file_index WHERE");
        List<Object> params = new ArrayList<>();

        if (!contentTerms.isEmpty()) {
            String combined = String.join(" ", contentTerms);
            sql.append(" MATCH(file_name, file_content) AGAINST (? IN NATURAL LANGUAGE MODE)");
            params.add(combined);
        }
        else throw new ContentNotPresentException("Please introduce content message");



        if (!pathTerms.isEmpty()) {
            for (String term : pathTerms) {
                sql.append(" AND file_path LIKE ?");
                params.add("%" + term + "%");
            }
        }
       

        if (!fileTypes.isEmpty()) {
            sql.append(" AND file_type IN (");
            sql.append(fileTypes.stream().map(f -> "?").collect(Collectors.joining(",")));
            sql.append(")");
            params.addAll(fileTypes);
        }

        List<FileIndex> sqlResult = jdbcTemplate.query(sql.toString(), new FileIndexRowMapper(), params.toArray());
        return sqlResult;
    }


}

