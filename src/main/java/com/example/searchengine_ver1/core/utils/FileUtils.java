package com.example.searchengine_ver1.core.utils;


import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

    /**
     * Extracts the text content from a file using Apache Tika.
     */
    public static String extractText(File file) {
        try (InputStream stream = new FileInputStream(file)) {
            BodyContentHandler handler = new BodyContentHandler(-1); // No character limit
            Metadata metadata = new Metadata();
            AutoDetectParser parser = new AutoDetectParser(); // Automatically detects file type
            ParseContext context = new ParseContext();

            parser.parse(stream, handler, metadata, context);

            return handler.toString();
        } catch (Exception e) {
            System.err.println("Error extracting content from: " + file.getAbsolutePath());
            return "";
        }
    }

    /**
     * Extracts metadata from a file using Apache Tika.
     */
    public static Metadata extractMetadata(File file) {
        try (InputStream stream = new FileInputStream(file)) {
            Metadata metadata = new Metadata();
            AutoDetectParser parser = new AutoDetectParser();
            ParseContext context = new ParseContext();
            parser.parse(stream, new BodyContentHandler(), metadata, context);
            return metadata;
        } catch (Exception e) {
            System.err.println("Error extracting metadata from: " + file.getAbsolutePath());
            return new Metadata();
        }
    }

    /**
     * Gets file extension from file name.
     */
    public static String getFileExtension(File file) {
        String name = file.getName();
        int lastDot = name.lastIndexOf('.');
        return (lastDot == -1) ? "" : name.substring(lastDot + 1);
    }
}