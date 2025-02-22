package com.example.demo.model;

import org.springframework.http.HttpStatus;

import java.util.Date;


public class ErrorMessage {
    private String message;
    private HttpStatus httpStatus;
    private Date date;

    public ErrorMessage(String message, HttpStatus httpStatus, Date date) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.date = date;
    }
}
