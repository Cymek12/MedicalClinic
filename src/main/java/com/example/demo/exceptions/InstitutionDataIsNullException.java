package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class InstitutionDataIsNullException extends WebException{
    public InstitutionDataIsNullException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
