package com.example.demo.exceptions.visit;

import com.example.demo.exceptions.WebException;
import org.springframework.http.HttpStatus;

public class VisitOverlapsException extends WebException {
    public VisitOverlapsException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
