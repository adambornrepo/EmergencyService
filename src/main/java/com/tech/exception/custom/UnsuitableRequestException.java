package com.tech.exception.custom;

public class UnsuitableRequestException extends RuntimeException {
    public UnsuitableRequestException(String message) {
        super(message);
    }

    public UnsuitableRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
