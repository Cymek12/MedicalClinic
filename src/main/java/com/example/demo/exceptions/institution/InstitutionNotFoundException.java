package com.example.demo.exceptions.institution;

import com.example.demo.exceptions.WebException;
import org.springframework.http.HttpStatus;

public class InstitutionNotFoundException extends WebException {
    public InstitutionNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
