package com.nexride.model;

public class ApiException extends RuntimeException {
    private final int status;

    public ApiException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() { return status; }
    public static ApiException bad(String message) { return new ApiException(message, 400); }
    public static ApiException forbidden(String message) { return new ApiException(message, 403); }
}
