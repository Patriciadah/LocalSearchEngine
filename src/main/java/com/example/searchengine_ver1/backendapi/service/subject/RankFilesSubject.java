package com.example.searchengine_ver1.backendapi.service.subject;

import com.example.searchengine_ver1.backendapi.service.observer.RankFilesObserver;
import com.example.searchengine_ver1.backendapi.service.observer.SuggestQueryObserver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class RankFilesSubject {
    List<RankFilesObserver> rankFilesObservers;

    public RankFilesSubject  (){
        this.rankFilesObservers = new ArrayList<>();
    }
    public void notifyObservers(List<String> results) {
        // Notify all observers
        rankFilesObservers.forEach(observer -> observer.onSearch(results));
    }

    public void registerObserver(RankFilesObserver o){
        rankFilesObservers.add(o);
    }
    public void removeObserver(RankFilesObserver o){
        int i = rankFilesObservers.indexOf(o);
        if (i>= 0){
            rankFilesObservers.remove(i);
        }
    }
}
