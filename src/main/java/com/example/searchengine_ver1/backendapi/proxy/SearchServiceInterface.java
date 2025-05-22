package com.example.searchengine_ver1.backendapi.proxy;

import com.example.searchengine_ver1.core.model.FileIndex;
import com.example.searchengine_ver1.exception.ContentNotPresentException;


import java.util.List;

public interface SearchServiceInterface {
    List<FileIndex> search(String query) throws ContentNotPresentException;
}
