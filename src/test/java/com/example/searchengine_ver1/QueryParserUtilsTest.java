package com.example.searchengine_ver1;


import com.example.searchengine_ver1.core.utils.QueryParserUtils;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class QueryParserUtilsTest {

    @Test
    public void testSingleQualifier() {
        String query = "content:indexing";
        Map<String, List<String>> result = QueryParserUtils.parseQuery(query);

        assertTrue(result.containsKey("content"));
        assertEquals(1, result.get("content").size());
        assertEquals("indexing", result.get("content").get(0));
    }

    @Test
    public void testMultipleQualifiers() {
        String query = "path:/src content:index content:search";
        Map<String, List<String>> result = QueryParserUtils.parseQuery(query);

        assertTrue(result.containsKey("path"));
        assertEquals("/src", result.get("path").get(0));

        assertTrue(result.containsKey("content"));
        assertEquals(List.of("index", "search"), result.get("content"));
    }

    @Test
    public void testInvalidSyntaxIgnored() {
        String query = "noColon this:isValid";
        Map<String, List<String>> result = QueryParserUtils.parseQuery(query);

        assertFalse(result.containsKey("noColon"));
        assertTrue(result.containsKey("this"));
        assertEquals("isValid", result.get("this").get(0));
    }

    @Test
    public void testEmptyQuery() {
        Map<String, List<String>> result = QueryParserUtils.parseQuery("");
        assertTrue(result.isEmpty());
    }
}

