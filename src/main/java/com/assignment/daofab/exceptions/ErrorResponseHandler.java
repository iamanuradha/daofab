package com.assignment.daofab.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class ErrorResponseHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ParentNotFound.class)
  public final ResponseEntity<Object> handleUserNotFoundException(
      ParentNotFound ex, WebRequest request) {
    ExceptionResponse exceptionResponse =
        new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
    return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InvalidPageException.class)
  public final ResponseEntity<Object> handleInvalidPageException(
          InvalidPageException ex, WebRequest request) {
    ExceptionResponse exceptionResponse =
            new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
    return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
  }
}
