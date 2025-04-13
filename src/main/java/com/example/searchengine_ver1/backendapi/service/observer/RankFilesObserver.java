package com.example.searchengine_ver1.backendapi.service.observer;

import java.util.List;

public interface RankFilesObserver {
    public void onSearch(List<String> files);
}
