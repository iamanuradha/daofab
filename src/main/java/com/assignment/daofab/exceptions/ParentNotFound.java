package com.assignment.daofab.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ParentNotFound extends Exception {
  public ParentNotFound(String message) {
    super(message);
  }
}
