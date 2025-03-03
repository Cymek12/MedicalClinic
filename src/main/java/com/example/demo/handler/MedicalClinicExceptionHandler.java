package com.example.demo.handler;

import com.example.demo.exceptions.*;
import com.example.demo.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class MedicalClinicExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(WebException.class)
    public ErrorMessage handleWebException(WebException ex) {
        return new ErrorMessage(ex.getMessage(), ex.getHttpStatus(), new Date());
    }
}