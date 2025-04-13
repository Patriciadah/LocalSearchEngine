package com.example.searchengine_ver1.backendapi.service.subject;

import com.example.searchengine_ver1.backendapi.service.observer.SuggestQueryObserver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class SuggestQuerySubject {
    List<SuggestQueryObserver> suggestQueryObservers;

    public SuggestQuerySubject (){
        this.suggestQueryObservers= new ArrayList<>();
    }
    public void notifyObservers(String query) {
        // Notify all observers
        suggestQueryObservers.forEach(observer -> observer.onSearch(query));
    }

    public void registerObserver(SuggestQueryObserver o){
        suggestQueryObservers.add(o);
    }
    public void removeObserver(SuggestQueryObserver o){
        int i = suggestQueryObservers.indexOf(o);
        if (i>= 0){
            suggestQueryObservers.remove(i);
        }
    }
    public void useQuery(String query){
        notifyObservers(query);
    }
}
