package com.example.demo.exceptions.institution;

import com.example.demo.exceptions.WebException;
import org.springframework.http.HttpStatus;

public class InstitutionAlreadyExistException extends WebException {
    public InstitutionAlreadyExistException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
