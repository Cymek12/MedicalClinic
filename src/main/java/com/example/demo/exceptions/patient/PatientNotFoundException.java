package com.example.demo.exceptions.patient;

import com.example.demo.exceptions.WebException;
import org.springframework.http.HttpStatus;

public class PatientNotFoundException extends WebException {
    public PatientNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
