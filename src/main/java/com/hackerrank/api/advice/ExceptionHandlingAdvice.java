package com.hackerrank.api.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionHandlingAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason = "Requested element was not found.")
    public void handleNoSuchElement(){}
}
