package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class InstitutionNotFoundException extends WebException{
    public InstitutionNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
