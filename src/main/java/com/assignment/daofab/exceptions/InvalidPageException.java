package com.assignment.daofab.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPageException extends Exception {
    public InvalidPageException(String message) {
        super(message);
    }
}
