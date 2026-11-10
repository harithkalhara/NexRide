package com.nexride.controller;

import com.nexride.service.ApiException;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ApiErrors {
  @ExceptionHandler(ApiException.class)
  public ResponseEntity<?> handle(ApiException e) {
    return ResponseEntity.status(e.getStatus()).body(Map.of("message", e.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> unexpected(Exception e) {
    return ResponseEntity.internalServerError()
        .body(Map.of("message", "NexRide could not complete that request."));
  }
}
