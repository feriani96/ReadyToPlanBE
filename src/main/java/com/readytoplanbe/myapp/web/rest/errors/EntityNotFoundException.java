package com.readytoplanbe.myapp.web.rest.errors;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}
