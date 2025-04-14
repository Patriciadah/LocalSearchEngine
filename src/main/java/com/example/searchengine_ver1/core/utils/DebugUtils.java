package com.example.searchengine_ver1.core.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DebugUtils {
    public static void clearContent(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("myStrings.txt", false))) {
        } catch (IOException e) {
            System.err.println("Error clearing file content for " + "myStrings.txt" + ": " + e.getMessage());
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("myContent.txt", false))) {
        } catch (IOException e) {
            System.err.println("Error clearing file content for " + "myContent.txt" + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void writeInFile(String prop){
        String filename = "myStrings.txt"; // Specify the desired filename
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename,true))) {

            writer.write(prop);
            writer.newLine();

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            e.printStackTrace(); // Print the full stack trace for debugging
        }
    }

    public static void writeContent(String prop){
        String filename = "myContent.txt"; // Specify the desired filename
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename,true))) {

            writer.write(prop);
            writer.newLine();



        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            e.printStackTrace(); // Print the full stack trace for debugging
        }
    }
}
