package com.example.searchengine_ver1.core.utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParserUtils {

    public static Map<String, List<String>> parseQuery(String query) {
        Map<String, List<String>> map = new HashMap<>();

        Pattern pattern = Pattern.compile("(\\w+):\"([^\"]+)\"|(\\w+):([\\w><=d.]+)");
        Matcher matcher = pattern.matcher(query);

        while (matcher.find()) {
            String key = matcher.group(1) != null ? matcher.group(1) : matcher.group(3);
            String value = matcher.group(2) != null ? matcher.group(2) : matcher.group(4);
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
        }

        return map;
    }

    public static OptionalDouble extractMinScore(Map<String, List<String>> parsed) {
        if (!parsed.containsKey("score")) return OptionalDouble.empty();
        for (String scoreVal : parsed.get("score")) {
            if (scoreVal.startsWith(">")) {
                try {
                    return OptionalDouble.of(Double.parseDouble(scoreVal.substring(1)));
                } catch (NumberFormatException ignored) {}
            }
        }
        return OptionalDouble.empty();
    }

    public static OptionalInt extractModifiedSinceDays(Map<String, List<String>> parsed) {
        if (!parsed.containsKey("modifiedSince")) return OptionalInt.empty();
        for (String val : parsed.get("modifiedSince")) {
            if (val.endsWith("d")) {
                try {
                    return OptionalInt.of(Integer.parseInt(val.replace("d", "")));
                } catch (Exception ignored) {}
            }
        }
        return OptionalInt.empty();
    }
}
