package com.example.searchengine_ver1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SearchEngineVer1Application {

    public static void main(String[] args) {
        SpringApplication.run(SearchEngineVer1Application.class, args);
    }

}
