package com.example.searchengine_ver1.backendapi.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class SearchSubject {
    List<SearchObserver> observers;

    public SearchSubject(){
        this.observers= new ArrayList<>();
    }
    public List<String> search(String query) {
        // Notify all observers
        observers.forEach(observer -> observer.onSearch(query));

        // Perform actual search logic here
        // For now, return dummy result
        return List.of("Result 1 for " + query, "Result 2 for " + query);
    }
}
