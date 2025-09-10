package org.example.spring.controller;

import org.example.spring.Exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmployeeAlreadyDeletedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleEmployeeAlreadyDeletedException(EmployeeAlreadyDeletedException e) {
        return e.getMessage();
    }

    @ExceptionHandler(EmployeeSalaryToLowException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleEmployeeSalaryToLowException(EmployeeSalaryToLowException e) {
        return e.getMessage();
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleEmployeeNotFoundException(EmployeeNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(EmployeeNotAmongLegalAgeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleEmployeeNotAmongLegalAgeException(EmployeeNotAmongLegalAgeException e) {
        return e.getMessage();
    }

    @ExceptionHandler(InactiveEmployeeUpdateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInactiveEmployeeUpdateException(InactiveEmployeeUpdateException e) {
        return e.getMessage();
    }


}
