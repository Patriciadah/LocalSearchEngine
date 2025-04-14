package com.example.searchengine_ver1.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryParserUtils {
    public static Map<String, List<String>> parseQuery(String query) {
        Map<String, List<String>> map = new HashMap<>();
        String[] parts = query.split("\\s+");

        for (String part : parts) {
            String[] keyValue = part.split(":", 2);
            if (keyValue.length == 2) {
                map.computeIfAbsent(keyValue[0], k -> new ArrayList<>()).add(keyValue[1]);
            }
        }
        return map;
    }
}

