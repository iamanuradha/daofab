package com.assignment.daofab.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class InitializationException extends Exception {
  public InitializationException(String message, Exception e) {
    super(message, e);
  }
}
