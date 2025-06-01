package com.example.demo.exceptions.visit;

import com.example.demo.exceptions.WebException;
import org.springframework.http.HttpStatus;

public class VisitViolatesBreakException extends WebException {
    public VisitViolatesBreakException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
