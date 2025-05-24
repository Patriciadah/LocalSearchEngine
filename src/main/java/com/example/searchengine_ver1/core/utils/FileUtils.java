package com.example.searchengine_ver1.core.utils;


import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

public class FileUtils {
    private static final Map<String, String> mimeExtensionMap = Map.ofEntries(
            Map.entry("pdf", "PDF"),
            Map.entry("msword", "Word"),
            Map.entry("vnd.openxmlformats-officedocument.wordprocessingml.document", "DOCX"),
            Map.entry("vnd.ms-excel", "Excel"),
            Map.entry("vnd.openxmlformats-officedocument.spreadsheetml.sheet", "Excel"),
            Map.entry("jpeg", "Image"),
            Map.entry("png", "Image"),
            Map.entry("jpg", "Image"),
            Map.entry("plain", "Text"),
            Map.entry("html", "HTML"),
            Map.entry("bmp","Image"),
            Map.entry("plain; charset=UTF-8","Text"),
            Map.entry("plain; charset=ISO-8859-1","Text")
    );
    /**
     * Extracts the text content from a file using Apache Tika.
     */
    public static String extractText(File file) {
        // If it's a .txt file, read manually
        if (getFileExtension(file).equals("Text")) {
            try { //DebugUtils.writeContent("reading text file: " + file.getAbsolutePath() + "\n");
                return Files.readString(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);
            } catch (IOException e) {
                System.err.println("Error reading text file: " + file.getAbsolutePath()+ "\n");
               // DebugUtils.writeContent("Error reading text file: " + file.getAbsolutePath());
                return "";
            }
        }

        // Otherwise, use Tika for other formats
        try (InputStream stream = new FileInputStream(file)) {
            BodyContentHandler handler = new BodyContentHandler(-1);
            Metadata metadata = new Metadata();
            AutoDetectParser parser = new AutoDetectParser();
            ParseContext context = new ParseContext();

            parser.parse(stream, handler, metadata, context);
            DebugUtils.writeContent("reading content from " + file.getAbsolutePath() + "\n");
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
        Metadata metadata = extractMetadata(file);
        String mimeType = metadata.get("Content-Type");
        if (mimeType != null) {
            String subtype = mimeType.contains("/") ? mimeType.substring(mimeType.indexOf("/") + 1) : mimeType;
            DebugUtils.writeInFile(subtype);
            return mimeExtensionMap.getOrDefault(subtype, subtype);
        }

        return "Unknown";
    }

}