package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class DoctorNotFoundException extends WebException{
    public DoctorNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
