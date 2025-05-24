package com.example.searchengine_ver1.backendapi.config;

import com.example.searchengine_ver1.backendapi.proxy.CachedSearchProxy;
import com.example.searchengine_ver1.backendapi.proxy.SearchServiceInterface;
import com.example.searchengine_ver1.backendapi.proxy.RealSearchService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 *  Wire CachedSearchProxy with RealSearchProxy through Spring boot framework
 * */
@Configuration
public class SearchConfig {

    @Bean
    public SearchServiceInterface searchServiceInterface(RealSearchService realSearchService) {
        return new CachedSearchProxy(realSearchService);
    }
}

