package com.example.demo.handler;

import com.example.demo.exceptions.CannotChangeIdCardNoException;
import com.example.demo.exceptions.PatientAlreadyExistException;
import com.example.demo.exceptions.PatientDataIsNullException;
import com.example.demo.exceptions.PatientNotFoundException;
import com.example.demo.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class MedicalClinicExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(PatientNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handlePatientNotFoundException(PatientNotFoundException ex) {
        return new ErrorMessage(ex.getMessage(), HttpStatus.NOT_FOUND, new Date());
    }

    @ExceptionHandler(PatientAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handlePatientAlreadyExistException(PatientAlreadyExistException ex) {
        return new ErrorMessage(ex.getMessage(), HttpStatus.BAD_REQUEST, new Date());
    }

    @ExceptionHandler(CannotChangeIdCardNoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleCannotChangeIdCardNoException(CannotChangeIdCardNoException ex) {
        return new ErrorMessage(ex.getMessage(), HttpStatus.BAD_REQUEST, new Date());
    }

    @ExceptionHandler(PatientDataIsNullException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handlePatientDataIsNullException (PatientDataIsNullException ex) {
        return new ErrorMessage(ex.getMessage(), HttpStatus.BAD_REQUEST, new Date());
    }
}