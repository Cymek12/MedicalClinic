package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class DoctorDataIsNullException extends WebException{
    public DoctorDataIsNullException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
