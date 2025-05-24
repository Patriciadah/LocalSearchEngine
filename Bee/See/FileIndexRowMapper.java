package com.example.searchengine_ver1.backendapi.repository;

import com.example.searchengine_ver1.model.FileIndex;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FileIndexRowMapper implements RowMapper<FileIndex> {
    @Override
    public FileIndex mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new FileIndex(
                rs.getLong("id"),
                rs.getString("file_name"),
                rs.getString("file_path"),
                rs.getString("file_type"),
                rs.getString("file_content"),
                rs.getTimestamp("indexed_at").toLocalDateTime()
        );
    }
}
