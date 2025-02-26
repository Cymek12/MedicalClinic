package com.example.demo.exceptions;

public class CannotChangeIdCardNoException extends RuntimeException {
    public CannotChangeIdCardNoException(String message) {
        super(message);
    }
}
