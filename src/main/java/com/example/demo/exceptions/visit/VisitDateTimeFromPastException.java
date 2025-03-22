package com.example.demo.exceptions.visit;

import com.example.demo.exceptions.WebException;
import org.springframework.http.HttpStatus;

public class VisitDateTimeFromPastException extends WebException {
    public VisitDateTimeFromPastException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
