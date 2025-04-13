package com.example.searchengine_ver1.backendapi.service.observer;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class SearchSubject {
    List<SearchObserver> observers;
    public SearchSubject(){
        this.observers= new ArrayList<>();
    }
    public void notifyObservers(String query) {
        // Notify all observers
        observers.forEach(observer -> observer.onSearch(query));

    }
    public void registerObserver(SearchObserver o){
        observers.add(o);
    }
    public void removeObserver(SearchObserver o){
        int i = observers.indexOf(o);
        if (i>= 0){
            observers.remove(i);
        }
    }
    public void useQuery(String query){
        notifyObservers(query);
    }
}
