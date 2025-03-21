package com.example.searchengine_ver1.backendapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    @GetMapping("/hello")
    public String sendGreetings() {
        // Call the service to fetch the message from the database
        return "hello world";
    }
}
