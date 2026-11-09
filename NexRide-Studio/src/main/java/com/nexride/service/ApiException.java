package com.nexride.service;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
  private final HttpStatus status;

  public ApiException(HttpStatus status, String message) {
    super(message);
    this.status = status;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public static ApiException bad(String m) {
    return new ApiException(HttpStatus.BAD_REQUEST, m);
  }

  public static ApiException forbidden(String m) {
    return new ApiException(HttpStatus.FORBIDDEN, m);
  }

  public static ApiException missing(String m) {
    return new ApiException(HttpStatus.NOT_FOUND, m);
  }

  public static ApiException unauthorized() {
    return new ApiException(HttpStatus.UNAUTHORIZED, "Sign in to continue.");
  }
}
