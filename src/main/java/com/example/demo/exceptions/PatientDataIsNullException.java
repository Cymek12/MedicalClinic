package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class PatientDataIsNullException extends WebException{
    public PatientDataIsNullException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
