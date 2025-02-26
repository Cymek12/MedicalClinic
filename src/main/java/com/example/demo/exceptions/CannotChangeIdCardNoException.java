package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class CannotChangeIdCardNoException extends WebException {
    public CannotChangeIdCardNoException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
