package com.bus.times.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

  @ExceptionHandler(value = InternalException.class)
  public ResponseEntity<ErrorResponse> handleInternalException(InternalException e) {
    ErrorResponse errorResponse = ErrorResponse.builder().message(e.getMessage()).build();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }
}
