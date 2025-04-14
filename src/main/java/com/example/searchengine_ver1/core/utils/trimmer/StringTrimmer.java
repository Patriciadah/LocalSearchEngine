package com.example.searchengine_ver1.core.utils.trimmer;

import java.nio.charset.StandardCharsets;

public class StringTrimmer {

    public static String trimToKB(String input, int KB) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        int maxBytes = KB * 1024;
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);

        if (bytes.length <= maxBytes) {
            return input;
        }

        // Find the last valid UTF-8 character boundary
        int endIndex = maxBytes;
        while (endIndex > 0 && (bytes[endIndex] & 0xC0) == 0x80) {
            endIndex--;
        }

        // Safety fallback
        if (endIndex <= 0) {
            endIndex = maxBytes;
        }

        return new String(bytes, 0, endIndex, StandardCharsets.UTF_8);
    }
}
