package com.example.demo.exceptions;

public class PatientDataIsNullException extends RuntimeException{
    public PatientDataIsNullException(String message) {
        super(message);
    }
}
