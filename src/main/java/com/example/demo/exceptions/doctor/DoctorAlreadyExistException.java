package com.example.demo.exceptions.doctor;

import com.example.demo.exceptions.WebException;
import org.springframework.http.HttpStatus;

public class DoctorAlreadyExistException extends WebException {
    public DoctorAlreadyExistException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
