package com.example.searchengine_ver1.core.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LoggerUtils {
    public static void writeInFile(String prop){
        String filename = "logger.txt"; // Specify the desired filename
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename,true))) {

            writer.write(prop);
            writer.newLine();



        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            e.printStackTrace(); // Print the full stack trace for debugging
        }
    }
}
