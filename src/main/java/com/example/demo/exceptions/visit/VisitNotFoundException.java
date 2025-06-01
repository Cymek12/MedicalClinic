package com.example.demo.exceptions.visit;

import com.example.demo.exceptions.WebException;
import org.springframework.http.HttpStatus;

public class VisitNotFoundException extends WebException {
    public VisitNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
