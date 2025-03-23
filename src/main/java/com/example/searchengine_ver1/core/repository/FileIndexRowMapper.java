package com.example.searchengine_ver1.core.repository;


import com.example.searchengine_ver1.core.model.FileIndex;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Maps result set form SQL response to JAVA object -FileIndex- form model package.
 * SQL response is taken from FileIndexRepository class
 * */
public class FileIndexRowMapper implements RowMapper<FileIndex> {
    @Override
    public @NonNull FileIndex mapRow(@NonNull ResultSet rs, @NonNull int rowNum) throws SQLException {
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
