package com.example.searchengine_ver1.tools;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.*;

public class WordMapBuilder {

    public static void main(String[] args) {
        String filePath = "src/main/resources/big.txt"; // make sure this path is correct
        Map<String, Integer> wordCounts = new HashMap<>();

        try {
            String text = Files.readString(Paths.get(filePath));
            Matcher m = Pattern.compile("\\w+").matcher(text.toLowerCase());
            while (m.find()) {
                String word = m.group();
                wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
            }

            // Serialize map to file
            try (ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream("src/main/resources/wordMap.ser"))) {
                out.writeObject(wordCounts);
                System.out.println("Word frequency map serialized successfully.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

