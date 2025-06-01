package com.example.demo.handler;

import com.example.demo.exceptions.*;
import com.example.demo.model.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class MedicalClinicExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(exception = WebException.class)
    public ResponseEntity<Object> handleWebException(WebException ex, WebRequest request) {
        ErrorMessage body = new ErrorMessage(ex.getMessage(), ex.getHttpStatus(), new Date());
        return handleExceptionInternal(ex, body, new HttpHeaders(), ex.getHttpStatus(), request);
    }
}