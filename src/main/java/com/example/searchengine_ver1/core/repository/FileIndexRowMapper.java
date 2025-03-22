package com.example.searchengine_ver1.core.repository;


import com.example.searchengine_ver1.core.model.FileIndex;
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
                rs.getTimestamp("indexed_at").toLocalDateTime() // I didn't know this existed
        );
    }
}
