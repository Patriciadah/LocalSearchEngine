package com.example.searchengine_ver1.backendapi.proxy;

import com.example.searchengine_ver1.core.model.FileIndex;
import com.example.searchengine_ver1.exception.ContentNotPresentException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Primary
public class CachedSearchProxy implements SearchServiceInterface {

    private final SearchServiceInterface realSearchService;
    private final Map<String, List<FileIndex>> cache = new HashMap<>();

    public CachedSearchProxy(SearchServiceInterface realSearchService) {
        this.realSearchService = realSearchService;
    }

    @Override
    public List<FileIndex> search(String query) throws ContentNotPresentException {
        if (cache.containsKey(query)) {
            System.out.println("Returning from cache: " + query);
            return cache.get(query);
        }

        System.out.println("Fetching from real search: " + query);
        List<FileIndex> result = realSearchService.search(query);
        cache.put(query, result);
        return result;
    }
}
