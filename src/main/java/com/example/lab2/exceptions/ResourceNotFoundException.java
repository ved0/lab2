package com.example.lab2.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    private final String id;


    public ResourceNotFoundException(String message, String id) {
        super(message);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}