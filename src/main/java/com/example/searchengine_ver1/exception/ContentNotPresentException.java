package com.example.searchengine_ver1.exception;

public class ContentNotPresentException extends RuntimeException{
    public ContentNotPresentException(String message){
        super(message);
    }
}
