package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class DoctorAlreadyExistException extends WebException{
    public DoctorAlreadyExistException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
