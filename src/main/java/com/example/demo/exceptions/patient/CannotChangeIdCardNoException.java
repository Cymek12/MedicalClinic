package com.example.demo.exceptions.patient;

import com.example.demo.exceptions.WebException;
import org.springframework.http.HttpStatus;

public class CannotChangeIdCardNoException extends WebException {
    public CannotChangeIdCardNoException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
