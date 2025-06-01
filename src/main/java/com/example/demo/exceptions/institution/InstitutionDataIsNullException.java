package com.example.demo.exceptions.institution;

import com.example.demo.exceptions.WebException;
import org.springframework.http.HttpStatus;

public class InstitutionDataIsNullException extends WebException {
    public InstitutionDataIsNullException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
